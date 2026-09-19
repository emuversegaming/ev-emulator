package com.swordfish.lemuroid.app.ads

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.swordfish.lemuroid.R

/** Keeps both the picture and touch controls inside the space above the ad. */
@Composable
fun BannerScreen(activity: Activity, collectConsent: Boolean, content: @Composable () -> Unit) {
    val consent = remember(activity) { UserMessagingPlatform.getConsentInformation(activity) }
    var adsAllowed by remember(activity) { mutableStateOf(false) }
    var privacyRequired by remember(activity) { mutableStateOf(false) }
    var disposed by remember(activity) { mutableStateOf(false) }
    var adGeneration by remember(activity) { mutableStateOf(0) }

    fun refreshConsent() {
        if (!disposed) {
            adsAllowed = consent.canRequestAds()
            privacyRequired = consent.privacyOptionsRequirementStatus ==
                ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
        }
    }

    DisposableEffect(activity) {
        onDispose { disposed = true }
    }
    LaunchedEffect(activity) {
        consent.requestConsentInfoUpdate(
            activity,
            ConsentRequestParameters.Builder().build(),
            {
                if (!disposed && !activity.isFinishing && !activity.isDestroyed) {
                    if (collectConsent) {
                        UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { refreshConsent() }
                    } else {
                        // The game runs in its own process. Refresh UMP there, but never
                        // interrupt gameplay with a consent dialog. MainActivity gathers it.
                        refreshConsent()
                    }
                }
            },
            { refreshConsent() },
        )
    }

    var sdkReady by remember(activity) { mutableStateOf(false) }
    LaunchedEffect(adsAllowed) {
        if (adsAllowed) {
            MobileAds.initialize(activity.applicationContext) {
                activity.runOnUiThread { if (!disposed) sdkReady = true }
            }
        }
    }

    Column(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f).fillMaxWidth()) { content() }
        if (collectConsent && privacyRequired) {
            TextButton(onClick = {
                adsAllowed = false
                UserMessagingPlatform.showPrivacyOptionsForm(activity) {
                    adGeneration++
                    refreshConsent()
                }
            }) {
                Text(stringResource(R.string.ad_privacy_choices))
            }
        }
        // Reserve space before loading so an arriving ad cannot shift touch controls.
        // A separate, non-interactive gutter prevents overlap with game controls.
        BoxWithConstraints(
            Modifier.fillMaxWidth().background(Color.Black)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom))
                .padding(top = 32.dp),
            contentAlignment = Alignment.Center,
        ) {
            val width = maxWidth.value.toInt()
            if (width >= 320) {
                val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, width)
                Box(Modifier.fillMaxWidth().height(adSize.height.dp)) {
                    if (adsAllowed && sdkReady) {
                        val adView = remember(activity, width, adSize.height, adGeneration) {
                            AdView(activity).apply {
                                setAdSize(adSize)
                                adUnitId = activity.getString(R.string.admob_banner_id)
                            }
                        }
                        DisposableEffect(adView) {
                            val lifecycle = (activity as LifecycleOwner).lifecycle
                            val observer = LifecycleEventObserver { _, event ->
                                when (event) {
                                    Lifecycle.Event.ON_RESUME -> adView.resume()
                                    Lifecycle.Event.ON_PAUSE -> adView.pause()
                                    else -> Unit
                                }
                            }
                            lifecycle.addObserver(observer)
                            adView.loadAd(AdRequest.Builder().build())
                            onDispose {
                                lifecycle.removeObserver(observer)
                                adView.destroy()
                            }
                        }
                        key(adView) {
                            AndroidView(
                                factory = { adView },
                                modifier = Modifier.fillMaxWidth().height(adSize.height.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

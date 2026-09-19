package com.swordfish.lemuroid.app.ads

import android.app.Activity
import androidx.compose.runtime.Composable

@Composable
fun BannerScreen(activity: Activity, collectConsent: Boolean, content: @Composable () -> Unit) {
    content()
}

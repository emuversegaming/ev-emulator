package com.swordfish.lemuroid.app.mobile.feature.game

import androidx.compose.runtime.Composable
import com.swordfish.lemuroid.app.mobile.feature.gamemenu.GameMenuActivity
import com.swordfish.lemuroid.app.shared.game.BaseGameActivity
import com.swordfish.lemuroid.app.shared.game.BaseGameScreenViewModel

class GameActivity : BaseGameActivity() {
    @Composable
    override fun GameScreen(viewModel: BaseGameScreenViewModel) {
        com.swordfish.lemuroid.app.ads.BannerScreen(this, collectConsent = false) {
            MobileGameScreen(viewModel)
        }
    }

    override fun getDialogClass() = GameMenuActivity::class.java
}

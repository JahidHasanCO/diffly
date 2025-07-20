package dev.jahidhasanco.diffly.presentation

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import dev.jahidhasanco.diffly.di.AppModule
import dev.jahidhasanco.diffly.presentation.screen.DiffCheckerScreen

class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        MainViewModel(AppModule.calculateDiffUseCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    WindowCompat.setDecorFitsSystemWindows(window, true)
                    WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
                }
            }
            DiffCheckerScreen(viewModel)
        }
    }
}



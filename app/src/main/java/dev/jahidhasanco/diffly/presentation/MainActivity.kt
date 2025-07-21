package dev.jahidhasanco.diffly.presentation

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.jahidhasanco.diffly.di.AppModule
import dev.jahidhasanco.diffly.navigation.NavControllerRouter
import dev.jahidhasanco.diffly.navigation.Screen
import dev.jahidhasanco.diffly.presentation.screen.DiffCheckerScreen
import dev.jahidhasanco.diffly.presentation.screen.DiffViewerScreen
import dev.jahidhasanco.diffly.presentation.viewmodel.DiffCheckerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    WindowCompat.setDecorFitsSystemWindows(window, true)
                    WindowInsetsControllerCompat(
                        window, view
                    ).isAppearanceLightStatusBars = true
                }
            }
            AppNavHost()
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val router = NavControllerRouter(navController)
    val diffCheckerViewModel by lazy {
        DiffCheckerViewModel(AppModule.calculateDiffUseCase, router)
    }
    NavHost(
        navController = navController,
        startDestination = Screen.DiffChecker.route
    ) {
        composable(Screen.DiffChecker.route) {

            DiffCheckerScreen(diffCheckerViewModel)
        }
        composable(Screen.DiffViewer.route) {
            DiffViewerScreen(diffCheckerViewModel)
        }
    }
}



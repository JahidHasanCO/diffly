package dev.jahidhasanco.diffly.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.jahidhasanco.diffly.di.AppModule
import dev.jahidhasanco.diffly.presentation.screen.DiffCheckerScreen

class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        MainViewModel(AppModule.calculateDiffUseCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiffCheckerScreen(viewModel)
        }
    }
}



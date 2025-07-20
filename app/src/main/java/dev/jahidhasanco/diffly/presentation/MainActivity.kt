package dev.jahidhasanco.diffly.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.jahidhasanco.diffly.di.AppModule
import dev.jahidhasanco.diffly.domain.model.DiffType

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

@Composable
fun DiffCheckerScreen(viewModel: MainViewModel) {
    var oldText by remember { mutableStateOf("") }
    var newText by remember { mutableStateOf("") }
    val diffResult by viewModel.diffResult.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Old Text")
        TextField(
            value = oldText,
            onValueChange = { oldText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("New Text")
        TextField(
            value = newText,
            onValueChange = { newText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.calculateDiff(oldText, newText) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Compare")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Old Text")
                diffResult.forEach { entry ->
                    entry.oldLine?.let {
                        Text(
                            it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    when (entry.type) {
                                        DiffType.DELETED -> Color(0xFFFFC0C0)
                                        else -> Color.Transparent
                                    }
                                )
                                .padding(4.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text("New Text")
                diffResult.forEach { entry ->
                    entry.newLine?.let {
                        Text(
                            it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    when (entry.type) {
                                        DiffType.ADDED -> Color(0xFFD0F0C0)
                                        else -> Color.Transparent
                                    }
                                )
                                .padding(4.dp)
                        )
                    }
                }
            }
        }

    }
}
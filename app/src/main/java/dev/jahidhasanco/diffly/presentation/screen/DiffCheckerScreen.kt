package dev.jahidhasanco.diffly.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.jahidhasanco.diffly.presentation.MainViewModel
import dev.jahidhasanco.diffly.presentation.component.CharDiffText

@Composable
fun DiffCheckerScreen(viewModel: MainViewModel) {
    var oldText by remember { mutableStateOf("") }
    var newText by remember { mutableStateOf("") }
    val diffResult by viewModel.diffResult.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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

        CharDiffText(diffResult)
    }
}

package dev.jahidhasanco.diffly.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.jahidhasanco.diffly.di.AppModule
import dev.jahidhasanco.diffly.domain.model.CharDiff
import dev.jahidhasanco.diffly.domain.model.CharDiffType
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

        // Use Box with weight to give LazyColumns height
        Box(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxSize()) {
                val scrollStateOld = rememberLazyListState()
                val scrollStateNew = rememberLazyListState()

                // Old Text Column
                LazyColumn(
                    state = scrollStateOld,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .border(1.dp, Color.Gray)
                ) {
                    item {
                        Text(
                            "Old Text",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    items(diffResult) { entry ->
                        entry.oldLine?.let {
                            val charDiffs = entry.charDiffs?.filter { it.type != CharDiffType.INSERTED }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            ) {
                                if (!charDiffs.isNullOrEmpty()) {
                                    InlineCharDiffText(charDiffs = charDiffs)
                                } else {
                                    Text(it)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // New Text Column
                LazyColumn(
                    state = scrollStateNew,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .border(1.dp, Color.Gray)
                ) {
                    item {
                        Text(
                            "New Text",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    items(diffResult) { entry ->
                        entry.newLine?.let {
                            val charDiffs = entry.charDiffs?.filter { it.type != CharDiffType.DELETED }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            ) {
                                if (!charDiffs.isNullOrEmpty()) {
                                    InlineCharDiffText(charDiffs = charDiffs)
                                } else {
                                    Text(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InlineCharDiffText(charDiffs: List<CharDiff>) {
    val annotatedString = buildAnnotatedString {
        charDiffs.forEach { cd ->
            val color = when (cd.type) {
                CharDiffType.UNCHANGED -> Color.Unspecified
                CharDiffType.INSERTED -> Color(0xFF00C281) // green
                CharDiffType.DELETED -> Color(0xFFFF6B6B) // red
            }
            withStyle(SpanStyle(color = color)) {
                append(cd.char)
            }
        }
    }
    Text(annotatedString)
}

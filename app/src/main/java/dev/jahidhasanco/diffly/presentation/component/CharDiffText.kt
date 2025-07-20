package dev.jahidhasanco.diffly.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.jahidhasanco.diffly.domain.model.CharDiffType
import dev.jahidhasanco.diffly.domain.model.DiffEntry
import dev.jahidhasanco.diffly.domain.model.DiffType
import dev.jahidhasanco.diffly.presentation.theme.added
import dev.jahidhasanco.diffly.presentation.theme.delete

@Composable
fun CharDiffText(diffResult: List<DiffEntry>) {
    Row(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            item {
                Text(
                    "Original Text",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(diffResult) { entry ->
                entry.oldLine?.let { line ->
                    val charDiffs = remember(entry.charDiffs) {
                        entry.charDiffs?.filter { it.type != CharDiffType.INSERTED }
                    }
                    val color = entry.type
                        .takeIf { it != DiffType.UNCHANGED }
                        ?.let {
                            when (it) {
                                DiffType.ADDED -> delete.copy(alpha = 0.3f)
                                DiffType.DELETED -> delete.copy(alpha = 0.3f)
                                DiffType.CHANGED -> delete.copy(alpha = 0.3f)
                                else -> Color.Unspecified
                            }
                        } ?: Color.Unspecified
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .background(color = color)
                    ) {
                        if (!charDiffs.isNullOrEmpty()) {
                            InlineCharDiffText(charDiffs = charDiffs)
                        } else {
                            Text(line)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // New text column
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            item {
                Text(
                    "Changed Text",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(diffResult) { entry ->
                entry.newLine?.let { line ->
                    val charDiffs = remember(entry.charDiffs) {
                        entry.charDiffs?.filter { it.type != CharDiffType.DELETED }
                    }
                    val color = entry.type
                        .takeIf { it != DiffType.UNCHANGED }
                        ?.let {
                            when (it) {
                                DiffType.ADDED -> added.copy(alpha = 0.3f)
                                DiffType.DELETED -> added.copy(alpha = 0.3f)
                                DiffType.CHANGED -> added.copy(alpha = 0.3f)
                                else -> Color.Unspecified
                            }
                        } ?: Color.Unspecified
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .background(color)

                    ) {
                        if (!charDiffs.isNullOrEmpty()) {
                            InlineCharDiffText(charDiffs = charDiffs)
                        } else {
                            Text(line)
                        }
                    }
                }
            }
        }
    }
}

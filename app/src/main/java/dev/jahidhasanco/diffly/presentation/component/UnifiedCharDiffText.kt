package dev.jahidhasanco.diffly.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeTheme
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString
import dev.jahidhasanco.diffly.domain.model.DiffEntry
import dev.jahidhasanco.diffly.domain.model.DiffType
import dev.jahidhasanco.diffly.presentation.theme.added
import dev.jahidhasanco.diffly.presentation.theme.delete

@Composable
fun UnifiedCharDiffText(
    isSyntaxHighlightEnabled: Boolean,
    language: CodeLang,
    parser: PrettifyParser,
    theme: CodeTheme,
    diffResult: List<DiffEntry>
) {
    var oldLineNumber = 1
    var newLineNumber = 1

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Text(
                "Unified View",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(diffResult) { entry ->
            // For CHANGED entries, show both old and new lines
            if (entry.type == DiffType.CHANGED) {
                // Show old line first with deletion styling
                entry.oldLine?.let { oldLine ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp, vertical = 1.dp)
                            .background(delete.copy(alpha = 0.1f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            // Old line number
                            Text(
                                text = oldLineNumber.toString(),
                                modifier = Modifier
                                    .width(40.dp)
                                    .padding(end = 2.dp),
                                color = Color.Gray
                            )

                            // New line number (empty for old line)
                            Text(
                                text = "",
                                modifier = Modifier
                                    .width(40.dp)
                                    .padding(end = 2.dp),
                                color = Color.Gray
                            )

                            // Prefix for deletion
                            Text(
                                text = "-",
                                modifier = Modifier.padding(end = 4.dp),
                                color = Color.Gray
                            )

                            // Old line content with syntax highlighting
                            if (isSyntaxHighlightEnabled) {
                                val syntaxAnnotatedString =
                                    remember(oldLine, language, theme) {
                                        parseCodeAsAnnotatedString(
                                            parser, theme, language, oldLine
                                        )
                                    }
                                Text(syntaxAnnotatedString)
                            } else {
                                Text(oldLine)
                            }
                        }
                    }
                }

                // Show new line second with addition styling
                entry.newLine?.let { newLine ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp, vertical = 1.dp)
                            .background(added.copy(alpha = 0.1f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            // Old line number (empty for new line)
                            Text(
                                text = "",
                                modifier = Modifier
                                    .width(40.dp)
                                    .padding(end = 2.dp),
                                color = Color.Gray
                            )

                            // New line number
                            Text(
                                text = newLineNumber.toString(),
                                modifier = Modifier
                                    .width(40.dp)
                                    .padding(end = 2.dp),
                                color = Color.Gray
                            )

                            // Prefix for addition
                            Text(
                                text = "+",
                                modifier = Modifier.padding(end = 4.dp),
                                color = Color.Gray
                            )

                            // New line content with char-level diff or syntax highlighting
                            if (!entry.charDiffs.isNullOrEmpty()) {
                                InlineCharDiffText(
                                    isSyntaxHighlightEnabled,
                                    newLine,
                                    charDiffs = entry.charDiffs,
                                    language,
                                    parser,
                                    theme
                                )
                            } else {
                                if (isSyntaxHighlightEnabled) {
                                    val syntaxAnnotatedString =
                                        remember(newLine, language, theme) {
                                            parseCodeAsAnnotatedString(
                                                parser, theme, language, newLine
                                            )
                                        }
                                    Text(syntaxAnnotatedString)
                                } else {
                                    Text(newLine)
                                }
                            }
                        }
                    }
                }

                // Update line numbers for CHANGED
                oldLineNumber++
                newLineNumber++
            } else {
                // Handle other diff types as before
                val prefix = when (entry.type) {
                    DiffType.ADDED -> "+"
                    DiffType.DELETED -> "-"
                    DiffType.UNCHANGED -> " "
                    else -> " " // This shouldn't happen now
                }

                val line = when (entry.type) {
                    DiffType.ADDED -> entry.newLine
                    DiffType.DELETED -> entry.oldLine
                    DiffType.UNCHANGED -> entry.oldLine
                    else -> null // This shouldn't happen now
                }

                val color = when (entry.type) {
                    DiffType.ADDED -> added.copy(alpha = 0.1f)
                    DiffType.DELETED -> delete.copy(alpha = 0.1f)
                    else -> Color.Transparent
                }

                line?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp, vertical = 1.dp)
                            .background(color)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            // Old line number
                            Text(text = entry.oldLine?.let { oldLineNumber.toString() }
                                ?: "",
                                modifier = Modifier
                                    .width(40.dp)
                                    .padding(end = 2.dp),
                                color = Color.Gray)

                            // New line number
                            Text(text = entry.newLine?.let { newLineNumber.toString() }
                                ?: "",
                                modifier = Modifier
                                    .width(40.dp)
                                    .padding(end = 2.dp),
                                color = Color.Gray)

                            // Prefix
                            Text(
                                text = prefix,
                                modifier = Modifier.padding(end = 4.dp),
                                color = Color.Gray
                            )

                            // Line content with syntax highlighting
                            if (isSyntaxHighlightEnabled) {
                                val syntaxAnnotatedString =
                                    remember(line, language, theme) {
                                        parseCodeAsAnnotatedString(
                                            parser, theme, language, line
                                        )
                                    }
                                Text(syntaxAnnotatedString)
                            } else {
                                Text(line)
                            }
                        }
                    }
                }

                // Update line numbers for other types
                when (entry.type) {
                    DiffType.ADDED -> newLineNumber++
                    DiffType.DELETED -> oldLineNumber++
                    DiffType.UNCHANGED -> {
                        oldLineNumber++
                        newLineNumber++
                    }
                    else -> {} // CHANGED is handled above
                }
            }
        }
    }
}


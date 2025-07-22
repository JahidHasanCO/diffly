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
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeTheme
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString
import dev.jahidhasanco.diffly.domain.model.CharDiffType
import dev.jahidhasanco.diffly.domain.model.DiffEntry
import dev.jahidhasanco.diffly.domain.model.DiffType
import dev.jahidhasanco.diffly.presentation.theme.added
import dev.jahidhasanco.diffly.presentation.theme.delete

@Composable
fun TwoSideCharDiffText(
    isSyntaxHighlightEnabled: Boolean,
    language: CodeLang,
    parser: PrettifyParser,
    theme: CodeTheme,
    diffResult: List<DiffEntry>
) {
    Row(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            item {
                Row {
                    Text(
                        "Original Text",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    val count =
                        diffResult.count { it.type == DiffType.CHANGED || it.type == DiffType.DELETED }
                    if (count > 0) Text(
                        "-${count}",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = delete,
                            fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                        )
                    )
                }
            }
            items(diffResult) { entry ->
                entry.oldLine?.let { line ->
                    val charDiffs = remember(entry.charDiffs) {
                        entry.charDiffs?.filter { it.type != CharDiffType.INSERTED }
                    }
                    val color =
                        entry.type.takeIf { it != DiffType.UNCHANGED }?.let {
                            when (it) {
                                DiffType.ADDED -> delete.copy(alpha = 0.05f)
                                DiffType.DELETED -> delete.copy(alpha = 0.05f)
                                DiffType.CHANGED -> delete.copy(alpha = 0.05f)
                                else -> Color.Unspecified
                            }
                        } ?: Color.Unspecified
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp, vertical = 1.dp)
                            .background(color = color)
                    ) {
                        if (!charDiffs.isNullOrEmpty()) {
                            InlineCharDiffText(
                                isSyntaxHighlightEnabled,
                                line,
                                charDiffs = charDiffs,
                                language,
                                parser,
                                theme
                            )
                        } else {
                            if (isSyntaxHighlightEnabled) {
                                val syntaxAnnotatedString =
                                    remember(line, language, theme) {
                                        parseCodeAsAnnotatedString(
                                            parser,
                                            theme,
                                            language,
                                            line
                                        )
                                    }
                                Text(syntaxAnnotatedString)
                            } else {
                                Text(line)
                            }
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
                Row {
                    Text(
                        "Changed Text",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    val count =
                        diffResult.count { it.type == DiffType.CHANGED || it.type == DiffType.ADDED }
                    if (count > 0) Text(
                        "+${count}",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = added,
                            fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                        )
                    )
                }
            }
            items(diffResult) { entry ->
                entry.newLine?.let { line ->
                    val charDiffs = remember(entry.charDiffs) {
                        entry.charDiffs?.filter { it.type != CharDiffType.DELETED }
                    }
                    val color =
                        entry.type.takeIf { it != DiffType.UNCHANGED }?.let {
                            when (it) {
                                DiffType.ADDED -> added.copy(alpha = 0.05f)
                                DiffType.DELETED -> added.copy(alpha = 0.05f)
                                DiffType.CHANGED -> added.copy(alpha = 0.05f)
                                else -> Color.Unspecified
                            }
                        } ?: Color.Unspecified
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp, vertical = 1.dp)
                            .background(color)

                    ) {
                        if (!charDiffs.isNullOrEmpty()) {
                            InlineCharDiffText(
                                isSyntaxHighlightEnabled,
                                line,
                                charDiffs = charDiffs,
                                language,
                                parser,
                                theme
                            )
                        } else {
                            if (isSyntaxHighlightEnabled) {
                                val syntaxAnnotatedString =
                                    remember(line, language, theme) {
                                        parseCodeAsAnnotatedString(
                                            parser,
                                            theme,
                                            language,
                                            line
                                        )
                                    }
                                Text(syntaxAnnotatedString)
                            } else {
                                Text(line)
                            }
                        }
                    }
                }
            }
        }
    }
}

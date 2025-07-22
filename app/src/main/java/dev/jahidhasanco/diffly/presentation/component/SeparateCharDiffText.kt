package dev.jahidhasanco.diffly.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun SeparateCharDiffText(
    isSyntaxHighlightEnabled: Boolean,
    language: CodeLang,
    parser: PrettifyParser,
    theme: CodeTheme,
    diffResult: List<DiffEntry>
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // Original Text Column
        Row {
            Text(
                "Original Text",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleMedium
            )
            val deletedCount =
                diffResult.count { it.type == DiffType.CHANGED || it.type == DiffType.DELETED }
            if (deletedCount > 0) {
                Text(
                    "-$deletedCount",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = delete,
                        fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                    )
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(diffResult) { entry ->
                entry.oldLine?.let { line ->
                    val charDiffs = remember(entry.charDiffs) {
                        entry.charDiffs?.filter { it.type != CharDiffType.INSERTED }
                    }
                    val color = when (entry.type) {
                        DiffType.ADDED, DiffType.DELETED, DiffType.CHANGED -> delete.copy(
                            alpha = 0.05f
                        )

                        else -> Color.Unspecified
                    }

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

        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(
                "Changed Text",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleMedium
            )
            val addedCount =
                diffResult.count { it.type == DiffType.CHANGED || it.type == DiffType.ADDED }
            if (addedCount > 0) {
                Text(
                    "+$addedCount",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = added,
                        fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                    )
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(diffResult) { entry ->
                entry.newLine?.let { line ->
                    val charDiffs = remember(entry.charDiffs) {
                        entry.charDiffs?.filter { it.type != CharDiffType.DELETED }
                    }
                    val color = when (entry.type) {
                        DiffType.ADDED, DiffType.DELETED, DiffType.CHANGED -> added.copy(
                            alpha = 0.05f
                        )

                        else -> Color.Unspecified
                    }

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

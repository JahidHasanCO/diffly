package dev.jahidhasanco.diffly.presentation.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import dev.jahidhasanco.diffly.domain.model.CharDiff
import dev.jahidhasanco.diffly.domain.model.CharDiffType
import dev.jahidhasanco.diffly.presentation.theme.added
import dev.jahidhasanco.diffly.presentation.theme.delete

@Composable
fun InlineCharDiffText(charDiffs: List<CharDiff>) {
    val annotatedString = buildAnnotatedString {
        charDiffs.forEach { cd ->
            val backgroundColor = when (cd.type) {
                CharDiffType.UNCHANGED -> Color.Unspecified
                CharDiffType.INSERTED -> added
                CharDiffType.DELETED -> delete
            }
            withStyle(
                SpanStyle(background = backgroundColor)
            ) {
                append(cd.char)
            }
        }
    }
    Text(annotatedString)
}

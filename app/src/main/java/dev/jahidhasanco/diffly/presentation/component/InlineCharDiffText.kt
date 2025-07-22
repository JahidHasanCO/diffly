package dev.jahidhasanco.diffly.presentation.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeTheme
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString
import dev.jahidhasanco.diffly.domain.model.CharDiff
import dev.jahidhasanco.diffly.domain.model.CharDiffType
import dev.jahidhasanco.diffly.presentation.theme.added
import dev.jahidhasanco.diffly.presentation.theme.delete

@Composable
fun InlineCharDiffText(
    code: String,
    charDiffs: List<CharDiff>,
    language: CodeLang,
    parser: PrettifyParser,
    theme: CodeTheme
) {
    val syntaxAnnotatedString = remember(code, language, theme) {
        parseCodeAsAnnotatedString(parser, theme, language, code)
    }

    val combinedAnnotatedString = buildAnnotatedString {
        for (i in code.indices) {
            val char = code[i]


            // get all syntax span styles at index i
            val spanStylesAtIndex =
                syntaxAnnotatedString.spanStyles.filter { it.start <= i && i < it.end }
                    .map { it.item }

            val charStyle = spanStylesAtIndex.fold(SpanStyle()) { acc, style ->
                acc.merge(style)
            }

            val backgroundColor = when (charDiffs.getOrNull(i)?.type) {
                CharDiffType.INSERTED -> added.copy(alpha = 0.2f)
                CharDiffType.DELETED -> delete.copy(alpha = 0.2f)
                CharDiffType.UNCHANGED -> Color.Transparent
                else -> Color.Transparent
            }

            val finalColor = if (charStyle.color == Color.Unspecified) {
                Color.Black
            } else {
                charStyle.color
            }

            val mergedStyle = charStyle.merge(
                SpanStyle(
                    color = finalColor,
                    background = backgroundColor
                )
            )


            withStyle(mergedStyle) {
                append(char)
            }
        }
    }

    Text(text = combinedAnnotatedString)
}

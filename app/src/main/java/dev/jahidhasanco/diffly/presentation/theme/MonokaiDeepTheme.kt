package dev.jahidhasanco.diffly.presentation.theme

import androidx.compose.ui.graphics.Color
import com.wakaztahir.codeeditor.highlight.theme.CodeTheme
import com.wakaztahir.codeeditor.highlight.theme.SyntaxColors

class MonokaiDeepTheme : CodeTheme() {
    override val colors = SyntaxColors(
        type = Color(0xFF03A9F4),       // deep green for types
        keyword = Color(0xFFE91E63),    // deep red-pink for keywords
        literal = Color(0xFF065BA1),    // deep blue for literals
        comment = Color(0xFF5C6370),    // dark gray for comments
        string = Color(0xFFB95E0A),     // deep orange for strings
        punctuation = Color(0xFFABB2BF),// muted light gray for punctuation
        plain = Color(0xFFABB2BF),      // plain text as light gray
        tag = Color(0xFFB03D45),        // same as keyword for tags
        declaration = Color(0xFFC82CEF),// deep purple for declarations
        source = Color(0xFFABB2BF),     // plain text
        attrName = Color(0xFF08B4D2),   // deep cyan for attribute names
        attrValue = Color(0xFFD0823A),  // deep orange for attribute values
        nocode = Color(0xFFABB2BF),     // plain text for no code
    )
}

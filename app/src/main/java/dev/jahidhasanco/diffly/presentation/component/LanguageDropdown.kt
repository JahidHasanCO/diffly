package dev.jahidhasanco.diffly.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.wakaztahir.codeeditor.highlight.model.CodeLang

@Composable
fun LanguageDropdown(
    selectedLanguage: CodeLang, onLanguageSelected: (CodeLang) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        TextButton(onClick = { expanded = !expanded }) {
            Text(selectedLanguage.name)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select Language"
            )
        }

        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            CodeLang.entries.forEach { lang ->
                DropdownMenuItem(
                    text = { Text(lang.name) },
                    trailingIcon = if (lang == selectedLanguage) {
                        { Icon(Icons.Default.Check, contentDescription = null) }
                    } else null,
                    onClick = {
                        onLanguageSelected(lang)
                        expanded = false
                    })
            }
        }
    }
}
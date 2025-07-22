package dev.jahidhasanco.diffly.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.jahidhasanco.diffly.domain.model.DiffViewType
import dev.jahidhasanco.diffly.presentation.theme.background
import dev.jahidhasanco.diffly.presentation.theme.primary

@Composable
fun DiffViewMenu(
    selectedViewType: DiffViewType,
    onViewTypeSelected: (DiffViewType) -> Unit,
    isSyntaxHighlightEnabled: Boolean,
    onSyntaxHighlightToggle: (Boolean) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                Icons.Default.MoreVert, contentDescription = "Menu"
            )
        }

        DropdownMenu(
            containerColor = background,
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Two Side View") },
                trailingIcon = if (selectedViewType == DiffViewType.TWO_SIDE) {
                    { Icon(Icons.Default.Check, contentDescription = null) }
                } else null,
                onClick = {
                    onViewTypeSelected(DiffViewType.TWO_SIDE)
                    expanded = false
                })
            DropdownMenuItem(
                text = { Text("Separate View") },
                trailingIcon = if (selectedViewType == DiffViewType.SEPARATE) {
                    { Icon(Icons.Default.Check, contentDescription = null) }
                } else null,
                onClick = {
                    onViewTypeSelected(DiffViewType.SEPARATE)
                    expanded = false
                })
            DropdownMenuItem(
                text = { Text("Unified View") },
                trailingIcon = if (selectedViewType == DiffViewType.UNIFIED) {
                    { Icon(Icons.Default.Check, contentDescription = null) }
                } else null,
                onClick = {
                    onViewTypeSelected(DiffViewType.UNIFIED)
                    expanded = false
                })
            HorizontalDivider()
            DropdownMenuItem(text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        "Syntax Highlight",
                        modifier = Modifier.padding(end = 2.dp)
                    )
                    Switch(
                        checked = isSyntaxHighlightEnabled,
                        onCheckedChange = { checked ->
                            onSyntaxHighlightToggle(checked)
                            expanded = false
                        },
                        modifier = Modifier.scale(0.7f),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = primary,
                            uncheckedThumbColor = Color.Gray,
                            checkedTrackColor = primary.copy(alpha = 0.3f),
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }
            }, onClick = {
                onSyntaxHighlightToggle(!isSyntaxHighlightEnabled)
                expanded = false
            })
        }
    }
}

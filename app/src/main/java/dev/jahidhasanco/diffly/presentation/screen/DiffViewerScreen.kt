package dev.jahidhasanco.diffly.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.jahidhasanco.diffly.domain.model.DiffViewType
import dev.jahidhasanco.diffly.presentation.component.SeparateCharDiffText
import dev.jahidhasanco.diffly.presentation.component.TwoSideCharDiffText
import dev.jahidhasanco.diffly.presentation.component.UnifiedCharDiffText
import dev.jahidhasanco.diffly.presentation.theme.background
import dev.jahidhasanco.diffly.presentation.viewmodel.DiffCheckerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiffViewerScreen(viewModel: DiffCheckerViewModel) {
    val selectedViewType by viewModel.selectedViewType.collectAsState()
    val diffResult by viewModel.diffResult.collectAsState()
    val expanded by viewModel.expanded.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = background,

        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = viewModel::goBack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                }, title = {
                    Text(
                        "Diff Viewer",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }, actions = {
                    // Popup Menu Icon
                    Box {
                        IconButton(onClick = { viewModel.setExpanded(!expanded) }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Menu"
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { viewModel.setExpanded(false) }) {
                            DropdownMenuItem(
                                text = { Text("Two Side View") },
                                trailingIcon = when (selectedViewType) {
                                    DiffViewType.TWO_SIDE -> {
                                        {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null
                                            )
                                        }
                                    }

                                    else -> null
                                },
                                onClick = {
                                    viewModel.selectViewType(
                                        DiffViewType.TWO_SIDE
                                    )
                                })
                            DropdownMenuItem(
                                text = { Text("Separate View") },
                                trailingIcon = when (selectedViewType) {
                                    DiffViewType.SEPARATE -> {
                                        {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null
                                            )
                                        }
                                    }

                                    else -> null
                                },
                                onClick = {
                                    viewModel.selectViewType(
                                        DiffViewType.SEPARATE
                                    )
                                })
                            DropdownMenuItem(
                                text = { Text("Unified View") },
                                trailingIcon = when (selectedViewType) {
                                    DiffViewType.UNIFIED -> {
                                        {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null
                                            )
                                        }
                                    }

                                    else -> null
                                },
                                onClick = {
                                    viewModel.selectViewType(
                                        DiffViewType.UNIFIED
                                    )
                                })
                        }
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                )

            )
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
        ) {
            when (selectedViewType) {
                DiffViewType.TWO_SIDE -> TwoSideCharDiffText(diffResult)
                DiffViewType.SEPARATE -> SeparateCharDiffText(diffResult)
                DiffViewType.UNIFIED -> UnifiedCharDiffText(diffResult)
            }
        }
    }
}
package dev.jahidhasanco.diffly.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import dev.jahidhasanco.diffly.domain.model.DiffViewType
import dev.jahidhasanco.diffly.presentation.component.DiffViewMenu
import dev.jahidhasanco.diffly.presentation.component.LanguageDropdown
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
    val language = viewModel.selectedLanguage.collectAsState()
    val parser = PrettifyParser()
    val theme = viewModel.theme
    val isSyntaxHighlightEnabled by viewModel.isSyntaxHighlightEnabled.collectAsState()

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
                if (isSyntaxHighlightEnabled) LanguageDropdown(
                    selectedLanguage = language.value,
                    onLanguageSelected = viewModel::selectLanguage
                )
                DiffViewMenu(
                    selectedViewType = selectedViewType,
                    onViewTypeSelected = viewModel::selectViewType,
                    isSyntaxHighlightEnabled = isSyntaxHighlightEnabled,
                    onSyntaxHighlightToggle = viewModel::setSyntaxHighlightEnabled
                )
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
                DiffViewType.TWO_SIDE -> TwoSideCharDiffText(
                    isSyntaxHighlightEnabled,
                    language.value,
                    parser,
                    theme,
                    diffResult
                )

                DiffViewType.SEPARATE -> SeparateCharDiffText(
                    isSyntaxHighlightEnabled,
                    language.value,
                    parser,
                    theme,
                    diffResult
                )

                DiffViewType.UNIFIED -> UnifiedCharDiffText(
                    isSyntaxHighlightEnabled,
                    language.value,
                    parser,
                    theme,
                    diffResult
                )
            }
        }
    }
}
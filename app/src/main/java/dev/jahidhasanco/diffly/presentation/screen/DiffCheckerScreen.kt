package dev.jahidhasanco.diffly.presentation.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString
import dev.jahidhasanco.diffly.domain.model.DiffViewType
import dev.jahidhasanco.diffly.presentation.component.DiffViewMenu
import dev.jahidhasanco.diffly.presentation.component.LanguageDropdown
import dev.jahidhasanco.diffly.presentation.component.SeparateCharDiffText
import dev.jahidhasanco.diffly.presentation.component.TwoSideCharDiffText
import dev.jahidhasanco.diffly.presentation.component.UnifiedCharDiffText
import dev.jahidhasanco.diffly.presentation.theme.background
import dev.jahidhasanco.diffly.presentation.theme.primary
import dev.jahidhasanco.diffly.presentation.viewmodel.DiffCheckerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiffCheckerScreen(viewModel: DiffCheckerViewModel) {
    val oldText by viewModel.oldText.collectAsState()
    val newText by viewModel.newText.collectAsState()
    val realTimeDiff by viewModel.realTimeDiff.collectAsState()
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
                title = {
                    Text(
                        "Diffly",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = primary,
                    )
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White, titleContentColor = primary
                ), actions = {
                    if (isSyntaxHighlightEnabled)
                        LanguageDropdown(
                            selectedLanguage = language.value,
                            onLanguageSelected = viewModel::selectLanguage
                        )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text = "Live",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (realTimeDiff) primary else Color.Gray,
                            modifier = Modifier.padding(end = 2.dp)
                        )
                        Switch(
                            modifier = Modifier.scale(0.7f),
                            checked = realTimeDiff,
                            onCheckedChange = viewModel::setRealTimeDiff,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = primary,
                                uncheckedThumbColor = Color.Gray,
                                checkedTrackColor = primary.copy(alpha = 0.3f),
                                uncheckedTrackColor = Color.LightGray
                            )
                        )
                    }

                    DiffViewMenu(
                        selectedViewType = selectedViewType,
                        onViewTypeSelected = viewModel::selectViewType,
                        isSyntaxHighlightEnabled = isSyntaxHighlightEnabled,
                        onSyntaxHighlightToggle = viewModel::setSyntaxHighlightEnabled
                    )
                })
        },
        floatingActionButton = {
            if (diffResult.isNotEmpty()) {
                IconButton(
                    onClick = viewModel::navigateToDiffViewer,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = primary, contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInFull,
                        contentDescription = "Expand",
                        tint = Color.White
                    )
                }
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                value = oldText,
                visualTransformation = if (isSyntaxHighlightEnabled) remember(
                    language.value
                ) {
                    VisualTransformation { text ->
                        val parsedText = parseCodeAsAnnotatedString(
                            parser, theme, language.value, text.text
                        )
                        TransformedText(parsedText, OffsetMapping.Identity)
                    }
                } else VisualTransformation.None,
                onValueChange = viewModel::updateOldText,
                label = {
                    Text(
                        "Original Text",
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),

                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = primary,
                    unfocusedIndicatorColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = primary,
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),

                maxLines = Int.MAX_VALUE,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(4.dp))
            IconButton(
                onClick = viewModel::swapTexts,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent, contentColor = primary
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Swap",
                    tint = primary,
                    modifier = Modifier
                        .padding(2.dp)
                        .size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = newText,
                visualTransformation = if (isSyntaxHighlightEnabled) remember(
                    language.value
                ) {
                    VisualTransformation { text ->
                        val parsedText = parseCodeAsAnnotatedString(
                            parser, theme, language.value, text.text
                        )
                        TransformedText(parsedText, OffsetMapping.Identity)
                    }
                } else VisualTransformation.None,
                onValueChange = viewModel::updateNewText,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),

                shape = RoundedCornerShape(8.dp),
                label = {
                    Text(
                        "Changed Text",
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = primary,
                    unfocusedIndicatorColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = primary,
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),

                maxLines = Int.MAX_VALUE,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!realTimeDiff) Button(
                onClick = { viewModel.calculateDiff(oldText, newText) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(0.6f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary, contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    focusedElevation = 0.dp,
                    hoveredElevation = 0.dp
                )
            ) {
                Text("Find Difference")
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
            ) {
                when (selectedViewType) {
                    DiffViewType.TWO_SIDE -> TwoSideCharDiffText(
                        isSyntaxHighlightEnabled,
                        language.value, parser, theme, diffResult
                    )

                    DiffViewType.SEPARATE -> SeparateCharDiffText(
                        isSyntaxHighlightEnabled,
                        language.value, parser, theme, diffResult
                    )

                    DiffViewType.UNIFIED -> UnifiedCharDiffText(
                        isSyntaxHighlightEnabled,
                        language.value, parser, theme, diffResult
                    )
                }
            }

        }
    }
}

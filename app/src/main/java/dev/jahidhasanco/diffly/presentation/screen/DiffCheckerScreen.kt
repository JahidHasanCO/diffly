package dev.jahidhasanco.diffly.presentation.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.jahidhasanco.diffly.presentation.MainViewModel
import dev.jahidhasanco.diffly.presentation.component.CharDiffText
import dev.jahidhasanco.diffly.presentation.theme.background
import dev.jahidhasanco.diffly.presentation.theme.primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiffCheckerScreen(viewModel: MainViewModel) {
    var oldText by remember { mutableStateOf("") }
    var newText by remember { mutableStateOf("") }
    val diffResult by viewModel.diffResult.collectAsState()
    var realTimeDiff by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = primary
                ),
                actions = {
                    Text(
                        text = "v1.0",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Real-time Diff",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (realTimeDiff) primary else Color.Gray,
                    modifier = Modifier.padding(end = 20.dp)
                )
                Switch(
                    checked = realTimeDiff,
                    onCheckedChange = { realTimeDiff = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = primary,
                        uncheckedThumbColor = Color.Gray,
                        checkedTrackColor = primary.copy(alpha = 0.3f),
                        uncheckedTrackColor = Color.LightGray
                    )
                )
            }

            OutlinedTextField(
                value = oldText,
                onValueChange = {
                    oldText = it
                    if (realTimeDiff) {
                        viewModel.calculateDiff(oldText, newText)
                    }
                },
                label = {
                    Text(
                        "Original Text",
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        color = Color.White, shape = RoundedCornerShape(8.dp)
                    ),

                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = primary,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedLabelColor = primary,
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),

                maxLines = Int.MAX_VALUE,
                singleLine = false
            )


            Spacer(modifier = Modifier.height(4.dp))

            // swap icon button
            IconButton(
                onClick = {
                    val temp = oldText
                    oldText = newText
                    newText = temp
                    if (realTimeDiff) {
                        viewModel.calculateDiff(oldText, newText)
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = primary
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Swap", tint = primary,
                    modifier = Modifier
                        .padding(2.dp)
                        .size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = newText, onValueChange = {
                    newText = it
                    if (realTimeDiff) {
                        viewModel.calculateDiff(oldText, newText)
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        color = Color.White, shape = RoundedCornerShape(8.dp)
                    ),

                shape = RoundedCornerShape(8.dp), label = {
                    Text(
                        "Changed Text",
                        style = MaterialTheme.typography.titleSmall
                    )
                }, colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = primary,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedLabelColor = primary,
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),

                maxLines = Int.MAX_VALUE, singleLine = false
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
            CharDiffText(diffResult)
        }
    }
}

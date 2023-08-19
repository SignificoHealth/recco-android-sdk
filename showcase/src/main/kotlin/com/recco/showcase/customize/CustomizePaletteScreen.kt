package com.recco.showcase.customize

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.recco.api.model.ReccoPalette
import com.recco.showcase.R
import com.recco.showcase.data.mappers.asShowcasePalette
import com.recco.showcase.models.ShowcasePalette
import com.recco.showcase.ui.components.ShowcaseButton
import com.recco.showcase.ui.components.ShowcaseTextField
import com.recco.showcase.ui.theme.BackgroundColor
import com.recco.showcase.ui.theme.SoftYellow
import com.recco.showcase.ui.theme.Typography
import com.recco.showcase.ui.theme.WarmBrown

@Composable
fun CustomizePaletteRoute(
    navigateUp: () -> Unit,
    viewModel: CustomizePaletteViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle()
    CustomizePaletteScreen(
        uiState = uiState,
        navigateUp = navigateUp,
        toggleMode = { viewModel.toggleMode() },
        onUpdatePalette = { viewModel.onUpdate(it) },
        onSavePalette = { viewModel.onSave(navigateUp) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomizePaletteScreen(
    uiState: CustomizePaletteUI,
    toggleMode: () -> Unit,
    navigateUp: () -> Unit,
    onUpdatePalette: (ShowcasePalette) -> Unit,
    onSavePalette: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = BackgroundColor),
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                title = { Text(stringResource(if (uiState.isEditing) R.string.edit_palette else R.string.create_palette)) }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(top = contentPadding.calculateTopPadding())
                .padding(all = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {

                ShowcaseTextField(
                    label = R.string.palette_name,
                    value = uiState.palette.name,
                    onValueChange = { newVale -> onUpdatePalette(uiState.palette.copy(name = newVale)) }
                )
                Spacer(Modifier.height(16.dp))
                Divider()
                Spacer(Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.alpha(if (uiState.isDarkMode) .5f else 1f),
                        text = stringResource(R.string.light).uppercase(),
                        style = Typography.titleMedium.copy(color = WarmBrown)
                    )
                    Spacer(Modifier.width(12.dp))
                    Switch(
                        checked = uiState.isDarkMode,
                        onCheckedChange = { toggleMode() },
                        colors = SwitchDefaults.colors(
                            uncheckedThumbColor = SoftYellow,
                            uncheckedTrackColor = WarmBrown,
                            uncheckedBorderColor = WarmBrown,
                            checkedThumbColor = SoftYellow,
                            checkedTrackColor = WarmBrown,
                            checkedBorderColor = WarmBrown
                        )
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        modifier = Modifier.alpha(if (uiState.isDarkMode) 1f else .5f),
                        text = stringResource(R.string.dark).uppercase(),
                        style = Typography.titleMedium.copy(color = WarmBrown)
                    )
                }
                Spacer(Modifier.height(12.dp))

                RowsColorContent(
                    palette = uiState.palette,
                    onUpdatePalette = onUpdatePalette,
                    isDarkMode = uiState.isDarkMode
                )
                Spacer(Modifier.height(32.dp))
            }

            ShowcaseButton(
                modifier = Modifier.padding(top = 24.dp),
                onClick = {
                    onSavePalette()
                    navigateUp()
                },
                text = stringResource(R.string.save_palette),
                enabled = uiState.palette.name.isNotBlank()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomizePaletteScreen(
        navigateUp = {},
        uiState = CustomizePaletteUI(palette = ReccoPalette.Fresh.asShowcasePalette()),
        toggleMode = {},
        onUpdatePalette = {},
        onSavePalette = {}
    )
}

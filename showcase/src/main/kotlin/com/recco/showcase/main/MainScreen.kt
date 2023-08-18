package com.recco.showcase.main

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ireward.htmlcompose.HtmlText
import com.recco.api.model.ReccoFont
import com.recco.api.model.ReccoStyle
import com.recco.showcase.R
import com.recco.showcase.ShowcaseApp
import com.recco.showcase.data.mappers.asReccoPalette
import com.recco.showcase.models.ShowcasePalette
import com.recco.showcase.ui.theme.BackgroundColor
import com.recco.showcase.ui.theme.SoftYellow
import com.recco.showcase.ui.theme.Typography
import com.recco.showcase.ui.theme.WarmBrown

@Composable
fun MainRoute(
    logoutClick: () -> Unit,
    openReccoClick: () -> Unit,
    createCustomPalette: () -> Unit,
    editCustomPalette: (ShowcasePalette) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val application = LocalContext.current.applicationContext as Application
    val uiState by viewModel.viewState.collectAsStateWithLifecycle()

    MainScreen(
        uiState = uiState,
        logoutClick = logoutClick,
        openReccoClick = openReccoClick,
        selectionClickPalette = { palette ->
            viewModel.setSelectedPaletteId(palette.id)

            ShowcaseApp.initSDK(
                application,
                ReccoStyle(
                    font = viewModel.selectedReccoFont,
                    palette = viewModel.selectedPalette.asReccoPalette()
                )
            )
        },
        selectionClickFont = { font ->
            viewModel.setSelectedReccoFont(font)

            ShowcaseApp.initSDK(
                application,
                ReccoStyle(
                    font = viewModel.selectedReccoFont,
                    palette = viewModel.selectedPalette.asReccoPalette()
                )
            )
        },
        createCustomPalette = createCustomPalette,
        editCustomPalette = editCustomPalette
    )
}

@Composable
private fun MainScreen(
    uiState: MainUI,
    logoutClick: () -> Unit,
    openReccoClick: () -> Unit,
    selectionClickPalette: (ShowcasePalette) -> Unit,
    selectionClickFont: (ReccoFont) -> Unit,
    createCustomPalette: () -> Unit,
    editCustomPalette: (ShowcasePalette) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(BackgroundColor)
                .padding(all = 24.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                painter = painterResource(id = R.drawable.bg_logo),
                contentDescription = ""
            )

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 42.dp, horizontal = 40.dp),
                painter = painterResource(id = R.drawable.bg_shapes),
                contentDescription = ""
            )

            HtmlText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.wellcome_message),
                style = Typography.bodyLarge.copy(textAlign = TextAlign.Center),
                URLSpanStyle = SpanStyle(
                    color = WarmBrown,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.None
                )
            )

            Spacer(Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = WarmBrown,
                    contentColor = SoftYellow
                ),
                shape = RoundedCornerShape(0.dp),
                onClick = openReccoClick
            ) {
                Text(
                    text = stringResource(id = R.string.open_recco),
                    style = Typography.displayMedium
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = WarmBrown
                ),
                shape = RoundedCornerShape(0.dp),
                border = BorderStroke(width = 1.dp, WarmBrown),
                onClick = logoutClick
            ) {
                Text(
                    text = stringResource(id = R.string.logout),
                    style = Typography.displayMedium.copy(color = WarmBrown)
                )
            }
        }

        Box {
            PaletteSelection(
                palettes = uiState.palettes,
                selectionClickPalette = selectionClickPalette,
                selectedPaletteId = uiState.selectedPaletteId,
                createCustomPalette = createCustomPalette,
                editCustomPalette = editCustomPalette
            )
            FontSelection(selectionClickFont, selectedFont = uiState.selectedFont)
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MainScreen(
        uiState = MainUI(),
        logoutClick = {},
        openReccoClick = {},
        selectionClickPalette = {},
        selectionClickFont = {},
        createCustomPalette = {},
        editCustomPalette = {}
    )
}

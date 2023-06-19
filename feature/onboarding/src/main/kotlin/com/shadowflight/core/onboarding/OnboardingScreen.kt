@file:OptIn(ExperimentalFoundationApi::class)

package com.shadowflight.core.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.shadowflight.core.ui.R.drawable
import com.shadowflight.core.ui.R.string
import com.shadowflight.core.ui.components.ASPECT_RATIO_1_1
import com.shadowflight.core.ui.components.AppTopBar
import com.shadowflight.core.ui.components.HorizontalPagerNavigation
import com.shadowflight.core.ui.theme.AppSpacing
import com.shadowflight.core.ui.theme.AppTheme

@Composable
fun OnboardingRoute(
    navigateToQuestionnaire: () -> Unit
) {
    val pagerState = rememberPagerState(0)

    OnboardingScreen(
        navigateToQuestionnaire = navigateToQuestionnaire,
        pagerState = pagerState
    )
}

@Composable
fun OnboardingScreen(
    navigateToQuestionnaire: () -> Unit,
    pagerState: PagerState = rememberPagerState()
) {

    val pages = listOf<@Composable (PaddingValues) -> Unit>({
        OnboardingPage(
            imageId = drawable.ic_flying,
            titleTextId = string.onboarding_welcome_title,
            bodyTextId = string.onboarding_welcome_body,
            contentPadding = it
        )
    }, {
        OnboardingPage(
            imageId = drawable.ic_content,
            titleTextId = string.onboarding_content_title,
            bodyTextId = string.onboarding_content_body,
            contentPadding = it
        )
    }, {
        OnboardingPage(
            imageId = drawable.ic_portrait_2,
            titleTextId = string.onboarding_about_title,
            bodyTextId = string.onboarding_about_body,
            contentPadding = it
        )
    })

    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = {
            HorizontalPagerNavigation(
                pagerState = pagerState,
                pageCount = pages.size,
                onButtonClick = navigateToQuestionnaire
            )
        }
    ) { contentPadding ->
        HorizontalPager(
            pageCount = pages.size,
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            pageContent = { pages[it](contentPadding) }
        )
    }
}

@Composable
private fun OnboardingPage(
    @DrawableRes imageId: Int,
    @StringRes titleTextId: Int,
    @StringRes bodyTextId: Int,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(top = contentPadding.calculateTopPadding())
            .padding(bottom = contentPadding.calculateBottomPadding())
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            modifier = Modifier
                .background(AppTheme.colors.accent20)
                .padding(horizontal = 64.dp)
                .aspectRatio(ratio = ASPECT_RATIO_1_1)
        )

        Spacer(modifier = Modifier.height(AppSpacing.dp_40))

        Text(
            text = stringResource(id = titleTextId),
            modifier = modifier
                .padding(horizontal = AppSpacing.dp_24),
            color = AppTheme.colors.primary,
            textAlign = TextAlign.Center,
            style = AppTheme.typography.h1
        )

        Spacer(modifier = Modifier.height(AppSpacing.dp_24))

        Text(
            text = stringResource(id = bodyTextId),
            modifier = modifier
                .padding(horizontal = AppSpacing.dp_24)
                .padding(bottom = AppSpacing.dp_12),
            color = AppTheme.colors.primary,
            textAlign = TextAlign.Center,
            style = AppTheme.typography.body2
        )
    }
}

@Preview(
    showBackground = true,
    name = "FlyingIntro"
)
@Composable
private fun AppOnboardingScreenOnePreview() {
    AppTheme {
        OnboardingScreen(
            navigateToQuestionnaire = {},
            pagerState = rememberPagerState(initialPage = 0)
        )
    }
}

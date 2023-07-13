@file:OptIn(ExperimentalFoundationApi::class)

package com.recco.internal.feature.onboarding

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
import com.google.accompanist.insets.ui.Scaffold
import com.recco.internal.core.ui.R.drawable
import com.recco.internal.core.ui.R.string
import com.recco.internal.core.ui.components.ASPECT_RATIO_1_1
import com.recco.internal.core.ui.components.AppTopBar
import com.recco.internal.core.ui.components.HorizontalPagerNavigation
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun OnboardingRoute(
    navigateToQuestionnaire: () -> Unit
) {
    val pagerState = rememberPagerState(0)

    OnboardingScreen(
        navigateToQuestionnaire = navigateToQuestionnaire,
        pagerState = pagerState
    )
}

@Composable
private fun OnboardingScreen(
    navigateToQuestionnaire: () -> Unit,
    pagerState: PagerState = rememberPagerState()
) {

    val pages = listOf<@Composable (PaddingValues) -> Unit>({
        OnboardingPage(
            imageId = drawable.recco_ic_flying,
            titleTextId = string.recco_onboarding_page1_title,
            bodyTextId = string.recco_onboarding_page1_body,
            contentPadding = it
        )
    }, {
        OnboardingPage(
            imageId = drawable.recco_ic_content,
            titleTextId = string.recco_onboarding_page2_title,
            bodyTextId = string.recco_onboarding_page2_body,
            contentPadding = it
        )
    }, {
        OnboardingPage(
            imageId = drawable.recco_ic_portrait_2,
            titleTextId = string.recco_onboarding_page3_title,
            bodyTextId = string.recco_onboarding_page3_body,
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
                .padding(top = AppSpacing.dp_32)
                .padding(horizontal = AppSpacing.dp_32 * 2)
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

@Preview
@Composable
private fun AppOnboardingScreenOnePreview() {
    AppTheme {
        OnboardingScreen(
            navigateToQuestionnaire = {},
            pagerState = rememberPagerState(initialPage = 0)
        )
    }
}

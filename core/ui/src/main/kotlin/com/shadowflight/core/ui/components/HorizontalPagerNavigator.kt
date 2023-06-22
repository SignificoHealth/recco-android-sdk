@file:OptIn(ExperimentalFoundationApi::class)

package com.shadowflight.core.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.shadowflight.core.ui.R
import com.shadowflight.core.ui.theme.AppSpacing
import com.shadowflight.core.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun HorizontalPagerNavigation(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    BackHandler(enabled = pagerState.currentPage > 0) {
        scope.launch { pagerState.animateScrollToPage(page = pagerState.currentPage.dec()) }
    }

    Column(
        modifier = modifier
            .background(Color.White)
    ) {
        val isLast = pagerState.currentPage == pageCount.dec()

        val buttonText = if (isLast) {
            stringResource(id = R.string.recco_onboarding_button_start)
        } else {
            stringResource(id = R.string.recco_onboarding_button_next)
        }

        val buttonAction = if (!isLast) {
            {
                scope.launch {
                    pagerState.animateScrollToPage(page = pagerState.currentPage.inc())
                }
            }
        } else {
            { onButtonClick() }
        }

        AppPrimaryButton(
            text = buttonText,
            onClick = { buttonAction.invoke() },
            modifier = modifier
                .padding(horizontal = AppSpacing.dp_24)
                .padding(bottom = AppSpacing.dp_24)
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.dp_24)
                .padding(bottom = AppSpacing.dp_32)
        ) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                pageCount = pageCount,
                modifier = Modifier.align(Alignment.Center),
                activeColor = AppTheme.colors.accent,
                inactiveColor = AppTheme.colors.primary20,
                indicatorShape = CircleShape,
                indicatorWidth = 34.dp,
                indicatorHeight = 4.dp
            )
        }
    }
}

@Preview
@Composable
private fun HorizontalPagerNavigationPreview() {
    AppTheme {
        HorizontalPagerNavigation(
            pagerState = rememberPagerState(0),
            pageCount = 3,
            onButtonClick = {}
        )
    }
}

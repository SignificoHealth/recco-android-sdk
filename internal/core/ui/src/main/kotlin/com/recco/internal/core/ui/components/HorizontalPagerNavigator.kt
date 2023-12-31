@file:OptIn(ExperimentalFoundationApi::class)

package com.recco.internal.core.ui.components

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerNavigation(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val isLast by remember { derivedStateOf { pagerState.currentPage.inc() == pagerState.pageCount } }

    BackHandler(enabled = pagerState.currentPage > 0) {
        scope.launch { pagerState.animateScrollToPage(page = pagerState.currentPage.dec()) }
    }

    Column(
        modifier = modifier
            .background(AppTheme.colors.background)
    ) {
        val buttonText = if (isLast) {
            stringResource(id = R.string.recco_start)
        } else {
            stringResource(id = R.string.recco_next)
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
                pageCount = pagerState.pageCount,
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

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun HorizontalPagerNavigationPreview() {
    AppTheme {
        HorizontalPagerNavigation(
            pagerState = rememberPagerState(initialPage = 0) { 3 },
            onButtonClick = {}
        )
    }
}

package com.shadowflight.core.ui.extensions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.shadowflight.core.ui.components.AppErrorContent
import com.shadowflight.core.ui.components.AppPrimaryButton
import com.shadowflight.core.ui.components.AppProgressLoadingCircled
import com.shadowflight.core.ui.theme.AppSpacing
import com.shadowflight.core.ui.theme.AppTheme

fun LazyListScope.setupLoadStates(
    isVerticalList: Boolean = true,
    pagingItems: LazyPagingItems<*>,
    swipeRefreshState: SwipeRefreshState? = null,
    initialLoadingState: @Composable (() -> Unit)? = null,
    loadingState: @Composable (() -> Unit)? = null,
    initialErrorState: @Composable ((Throwable) -> Unit)? = null,
    errorState: @Composable ((Throwable) -> Unit)? = null,
    emptySate: @Composable (() -> Unit)? = null,
    isEmptyState: Boolean? = null,
) {
    val defaultInitialErrorState: @Composable (Throwable) -> Unit = { throwable ->
        AppErrorContent(
            throwable = throwable,
            retry = { pagingItems.retry() }
        )
    }

    setupPagingLoadStates(
        pagingItems = pagingItems,
        swipeRefreshState = swipeRefreshState,
        initialLoadingState = {
            item { initialLoadingState?.invoke() }
        },
        loadingState = {
            item {
                loadingState?.invoke() ?: LoadingStateViewContent(isVerticalList = isVerticalList)
            }
        },
        initialErrorState = { throwable ->
            item { initialErrorState?.invoke(throwable) ?: defaultInitialErrorState(throwable) }
        },
        errorState = { throwable ->
            item {
                errorState?.invoke(throwable) ?: ErrorStateViewContent(
                    throwable = throwable,
                    callToActionButtonListener = { pagingItems.retry() }
                )
            }
        },
        emptySate = {
            item { emptySate?.invoke() }
        },
        isEmptyState = isEmptyState
    )
}

fun LazyGridScope.setupLoadStates(
    pagingItems: LazyPagingItems<*>,
    columns: Int,
    swipeRefreshState: SwipeRefreshState? = null,
    initialLoadingState: @Composable (() -> Unit)? = null,
    initialErrorState: @Composable ((Throwable) -> Unit)? = null,
    errorState: @Composable ((Throwable) -> Unit)? = null,
    emptySate: @Composable (() -> Unit)? = null,
    isEmptyState: Boolean? = null,
) {
    val defaultInitialErrorState: @Composable (Throwable) -> Unit = { throwable ->
        AppErrorContent(
            throwable = throwable,
            retry = { pagingItems.retry() }
        )
    }

    setupPagingLoadStates(
        pagingItems = pagingItems,
        swipeRefreshState = swipeRefreshState,
        initialLoadingState = {
            item(span = { GridItemSpan(columns) }) {
                initialLoadingState?.invoke()
            }
        },
        loadingState = {
            item(span = { GridItemSpan(columns) }) {
                LoadingStateViewContent(isVerticalList = false)
            }
        },
        initialErrorState = { throwable ->
            item(span = { GridItemSpan(columns) }) {
                initialErrorState?.invoke(throwable) ?: defaultInitialErrorState(throwable)
            }
        },
        errorState = { throwable ->
            item(span = { GridItemSpan(columns) }) {
                errorState?.invoke(throwable) ?: ErrorStateViewContent(
                    throwable = throwable,
                    callToActionButtonListener = { pagingItems.retry() }
                )
            }
        },
        emptySate = {
            item(span = { GridItemSpan(columns) }) {
                emptySate?.invoke()
            }
        },
        isEmptyState = isEmptyState
    )
}

/**
 * @param isEmptyState Use this parameter only in case you need additional logic to determine when
 * to show the empty state, e.g: when the screen show different sections, not only the paginated one
 */
private fun setupPagingLoadStates(
    pagingItems: LazyPagingItems<*>,
    swipeRefreshState: SwipeRefreshState? = null,
    initialLoadingState: (() -> Unit)? = null,
    loadingState: () -> Unit,
    initialErrorState: (Throwable) -> Unit,
    errorState: (Throwable) -> Unit,
    emptySate: (() -> Unit)? = null,
    isEmptyState: Boolean? = null,
) {
    pagingItems.apply {
        when {
            loadState.refresh is LoadState.Loading -> {
                swipeRefreshState?.isRefreshing = true
                initialLoadingState?.invoke()
            }

            loadState.append is LoadState.Loading -> {
                swipeRefreshState?.isRefreshing = false
                loadingState()
            }

            loadState.refresh is LoadState.Error -> {
                val throwable = (loadState.refresh as LoadState.Error).error
                swipeRefreshState?.isRefreshing = false
                initialErrorState(throwable)
            }

            loadState.append is LoadState.Error -> {
                val throwable = (loadState.append as LoadState.Error).error
                swipeRefreshState?.isRefreshing = false
                errorState(throwable)
            }

            loadState.refresh is LoadState.NotLoading -> {
                swipeRefreshState?.isRefreshing = false
                val isActuallyEmpty = pagingItems.itemCount == 0 &&
                        loadState.append.endOfPaginationReached
                val showEmptyState = (isEmptyState ?: true) && emptySate != null
                if (isActuallyEmpty && showEmptyState) {
                    emptySate!!()
                }
            }
        }
    }
}

@Composable
private fun ErrorStateViewContent(
    throwable: Throwable? = null,
    callToActionButtonListener: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppSpacing.dp_16),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = throwable.asDescriptionRes()),
            style = AppTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(AppSpacing.dp_16))

        AppPrimaryButton(
            onClick = { callToActionButtonListener() },
            textRes = throwable.asCtaTextRes(),
            iconStartRes = throwable.asCtaIconRes(),
        )
    }
}

@Composable
private fun LoadingStateViewContent(isVerticalList: Boolean) {
    val topPadding = if (isVerticalList) AppSpacing.dp_16 else 0.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(topPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppProgressLoadingCircled()
    }
}

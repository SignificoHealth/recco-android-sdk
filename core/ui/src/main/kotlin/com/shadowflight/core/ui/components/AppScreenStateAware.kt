package com.shadowflight.core.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.shadowflight.core.ui.R
import com.shadowflight.core.ui.extensions.noRippleClickable
import com.shadowflight.core.ui.extensions.setupLoadStates
import com.shadowflight.core.ui.theme.AppSpacing
import com.shadowflight.core.ui.theme.AppTheme
import kotlinx.coroutines.flow.flowOf
import java.lang.Float.min

data class UiState<T>(
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val data: T? = null
)

/**
 * Extracted from [SwipeRefresh] documentation:
 * A layout which implements the swipe-to-refresh pattern, allowing the user to refresh content via
 * a vertical swipe gesture.
 *
 * This layout requires its content to be scrollable so that it receives vertical swipe events.
 * The scrollable content does not need to be a direct descendant though. Layouts such as
 * [androidx.compose.foundation.lazy.LazyColumn] are automatically scrollable, but others such as
 * [androidx.compose.foundation.layout.Column] require you to provide the
 * [androidx.compose.foundation.verticalScroll] modifier to that content.
 */
@Composable
fun <T> AppScreenStateAware(
    modifier: Modifier = Modifier,
    scrollState: ScrollState? = null,
    isFloatingHeader: Boolean = false,
    uiState: UiState<T>,
    enablePullToRefresh: Boolean = false,
    avoidClickingWhenRefreshing: Boolean = true,
    isEmpty: Boolean = false,
    retry: () -> Unit,
    refresh: (() -> Unit)? = null,
    colorStatusBar: Color = AppTheme.colors.primary,
    backgroundContent: @Composable (() -> Unit)? = null,
    animatedContentShapeContent: @Composable (() -> Unit)? = null,
    animatedContent: @Composable ((uiStateData: T) -> Unit)? = null,
    emptyContent: @Composable (ColumnScope.() -> Unit)? = null,
    footerContent: @Composable ((uiStateData: T) -> Unit)? = null,
    isFloatingFooter: Boolean = false,
    content: @Composable ColumnScope.(uiStateData: T) -> Unit
) {
    val isFirstLoading = remember { mutableStateOf(true) }
    val isAnimatedContentCollapsed = remember { mutableStateOf(true) }

    AppTheme(colorStatusBar = colorStatusBar) {
        if (isFloatingHeader) {
            Box(modifier = Modifier.fillMaxSize()) {
                backgroundContent?.invoke()

                AppScreenStateAwareContent(
                    uiState = uiState,
                    isAnimatedContentCollapsed = isAnimatedContentCollapsed,
                    modifier = modifier,
                    isFloatingHeader = true,
                    scrollState = scrollState,
                    animatedContentShapeContent = animatedContentShapeContent,
                    animatedContent = animatedContent,
                    isFirstLoading = isFirstLoading,
                    isEmpty = isEmpty,
                    retry = retry,
                    refresh = refresh ?: {},
                    emptyContent = emptyContent,
                    enablePullToRefresh = enablePullToRefresh,
                    avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
                    colorStatusBar = colorStatusBar,
                    footerContent = footerContent,
                    isFloatingFooter = isFloatingFooter,
                    content = content
                )
                if (isFloatingFooter) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        contentAlignment = Alignment.Center
                    ) {
                        uiState.data?.let { footerContent?.invoke(it) }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                backgroundContent?.invoke()

                Column(modifier = Modifier.fillMaxSize()) {
                    AppScreenStateAwareContent(
                        isAnimatedContentCollapsed = isAnimatedContentCollapsed,
                        modifier = modifier,
                        isFloatingHeader = false,
                        scrollState = scrollState,
                        animatedContentShapeContent = animatedContentShapeContent,
                        animatedContent = animatedContent,
                        isFirstLoading = isFirstLoading,
                        isEmpty = isEmpty,
                        uiState = uiState,
                        retry = retry,
                        refresh = refresh ?: {},
                        emptyContent = emptyContent,
                        enablePullToRefresh = enablePullToRefresh,
                        avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
                        colorStatusBar = colorStatusBar,
                        footerContent = footerContent,
                        isFloatingFooter = isFloatingFooter,
                        content = content
                    )
                }

                if (isFloatingFooter) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        contentAlignment = Alignment.Center
                    ) {
                        uiState.data?.let { footerContent?.invoke(it) }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderAwareContent(
    isFloatingHeader: Boolean,
    content: @Composable () -> Unit
) {
    val extraHeight = remember {
        mutableStateOf(if (isFloatingHeader) AppSpacing.dp_40 else 0.dp)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AppSpacing.dp_24),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(extraHeight.value))
        content()
    }
}

@Composable
private fun <T> AppScreenStateAwareContent(
    modifier: Modifier,
    isAnimatedContentCollapsed: MutableState<Boolean>,
    isFloatingHeader: Boolean,
    scrollState: ScrollState?,
    isFirstLoading: MutableState<Boolean>,
    isEmpty: Boolean,
    uiState: UiState<T>,
    retry: () -> Unit,
    refresh: () -> Unit,
    enablePullToRefresh: Boolean,
    avoidClickingWhenRefreshing: Boolean,
    colorStatusBar: Color,
    animatedContentShapeContent: @Composable (() -> Unit)?,
    animatedContent: @Composable ((uiStateData: T) -> Unit)?,
    emptyContent: @Composable (ColumnScope.() -> Unit)?,
    content: @Composable ColumnScope.(data: T) -> Unit,
    footerContent: @Composable ((uiStateData: T) -> Unit)? = null,
    isFloatingFooter: Boolean = false,
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isLoading)
    swipeRefreshState.isRefreshing = uiState.isLoading
    val animatedContentHeight = remember { mutableStateOf(0f) }

    when {
        uiState.isLoading && isFirstLoading.value -> {
            HeaderAwareContent(isFloatingHeader) {
                AppProgressLoadingCircled(modifier = modifier)
            }
        }

        uiState.error != null -> {
            HeaderAwareContent(isFloatingHeader) {
                AppErrorContent(
                    throwable = uiState.error,
                    retry = retry,
                    scrollState = rememberScrollState()
                )
            }
        }

        isEmpty -> {
            SwipeRefreshContent(
                modifier = modifier,
                swipeRefreshState = swipeRefreshState,
                enablePullToRefresh = enablePullToRefresh,
                avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
                refresh = refresh,
                colorStatusBar = colorStatusBar
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    emptyContent?.invoke(this)
                }
            }
        }

        else -> {
            LaunchedEffect(Unit) {
                isFirstLoading.value = false
            }
            SwipeRefreshContent(
                modifier = modifier,
                swipeRefreshState = swipeRefreshState,
                enablePullToRefresh = enablePullToRefresh,
                avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
                refresh = refresh,
                colorStatusBar = colorStatusBar,
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .let {
                                if (scrollState != null) {
                                    it.verticalScroll(scrollState)
                                } else {
                                    it
                                }
                            }
                    ) {
                        if (scrollState != null) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .onGloballyPositioned { coordinates ->
                                            val height = coordinates.size.height.toFloat()
                                            // If there is no animated content we specify a default height
                                            // in order to allow elevate the header when scrolling
                                            animatedContentHeight.value =
                                                if (height <= 0f) 10f else height
                                        }
                                        .graphicsLayer {
                                            val scrollValue = scrollState.value.toFloat()
                                            val height = animatedContentHeight.value
                                            if (height > 0f) {
                                                alpha = min(
                                                    1f,
                                                    1f - ((scrollValue / height)) * 1.3f
                                                )
                                                isAnimatedContentCollapsed.value = alpha <= 0
                                                translationY = 0.5f * scrollState.value
                                            }
                                        }
                                ) {
                                    uiState.data?.let { animatedContent?.invoke(it) }
                                }
                                animatedContentShapeContent?.invoke()
                            }
                        }

                        uiState.data?.let { content(it) }
                    }

                    if (scrollState != null && !isFloatingFooter) {
                        AppElevatedBottomContent(scrollState = scrollState) {
                            uiState.data?.let { footerContent?.invoke(it) }
                        }
                    }
                }
            }
        }
    }
}

/**
 * @param isEmpty Use this parameter only in case you need additional logic to determine when
 * to show the empty state, e.g: when the screen show different sections, not only the paginated one
 */
@Composable
fun AppScreenStateAwarePaginatedList(
    modifier: Modifier = Modifier,
    emptyStateModifier: Modifier = Modifier,
    items: LazyPagingItems<*>,
    scrollState: LazyListState = rememberLazyListState(),
    enablePullToRefresh: Boolean = true,
    avoidClickingWhenRefreshing: Boolean = true,
    isEmpty: Boolean? = null,
    emptyState: EmptyState? = null,
    retry: () -> Unit,
    refresh: () -> Unit,
    colorStatusBar: Color = Color.White,
    headerContent: @Composable (() -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(false)

    Column(modifier = Modifier.fillMaxSize()) {
        headerContent?.invoke()

        SwipeRefreshContent(
            swipeRefreshState = swipeRefreshState,
            enablePullToRefresh = enablePullToRefresh,
            avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
            refresh = refresh,
            colorStatusBar = colorStatusBar
        ) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                state = scrollState,
            ) {
                content()

                setupLoadStates(
                    pagingItems = items,
                    swipeRefreshState = swipeRefreshState,
                    isEmptyState = isEmpty,
                    emptySate = emptyState?.let {
                        {
                            AppEmptyContent(
                                drawableModifier = emptyStateModifier,
                                emptyState = emptyState
                            )
                        }
                    }
                )
            }
        }
    }
}

/**
 * @param isEmpty Use this parameter only in case you need additional logic to determine when
 * to show the empty state, e.g: when the screen show different sections, not only the paginated one
 */
@Composable
fun AppScreenStateAwarePaginatedGrid(
    modifier: Modifier = Modifier,
    emptyStateModifier: Modifier = Modifier,
    columns: Int,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    scrollState: LazyGridState = rememberLazyGridState(),
    items: LazyPagingItems<*>,
    enablePullToRefresh: Boolean = true,
    avoidClickingWhenRefreshing: Boolean = true,
    isEmpty: Boolean? = null,
    emptyState: EmptyState? = null,
    refresh: () -> Unit,
    colorStatusBar: Color = Color.White,
    headerContent: @Composable (() -> Unit)? = null,
    content: LazyGridScope.() -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(false)

    SwipeRefreshContent(
        swipeRefreshState = swipeRefreshState,
        enablePullToRefresh = enablePullToRefresh,
        avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
        refresh = refresh,
        colorStatusBar = colorStatusBar
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            headerContent?.invoke()

            LazyVerticalGrid(
                modifier = modifier.fillMaxSize(),
                state = scrollState,
                columns = GridCells.Fixed(columns),
                contentPadding = contentPadding
            ) {
                content()

                setupLoadStates(
                    pagingItems = items,
                    columns = columns,
                    swipeRefreshState = swipeRefreshState,
                    isEmptyState = isEmpty,
                    emptySate = emptyState?.let {
                        {
                            AppEmptyContent(
                                drawableModifier = emptyStateModifier,
                                emptyState = emptyState
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SwipeRefreshContent(
    modifier: Modifier = Modifier,
    enablePullToRefresh: Boolean,
    swipeRefreshState: SwipeRefreshState,
    refresh: () -> Unit,
    avoidClickingWhenRefreshing: Boolean,
    colorStatusBar: Color,
    content: @Composable () -> Unit
) {
    AppTheme(colorStatusBar = colorStatusBar) {
        SwipeRefresh(
            modifier = modifier,
            swipeEnabled = enablePullToRefresh,
            state = swipeRefreshState,
            onRefresh = { refresh() },
            indicator = { state, refreshTrigger ->
                when {
                    !enablePullToRefresh -> {
                        AppSwipeRefreshLoadingIndicator(
                            state = state,
                            refreshTrigger = refreshTrigger,
                            elevation = 0.dp
                        )
                    }

                    avoidClickingWhenRefreshing && state.isRefreshing -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .noRippleClickable { },
                            contentAlignment = Alignment.TopCenter
                        ) {
                            AppSwipeRefreshLoadingIndicator(state, refreshTrigger)
                        }
                    }

                    else -> {
                        AppSwipeRefreshLoadingIndicator(state, refreshTrigger)
                    }
                }
            },
            content = content
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun LoadingPreview() {
    AppScreenStateAware(
        scrollState = rememberScrollState(),
        uiState = UiState<Unit>(isLoading = true),
        retry = { },
        refresh = { },
    ) {
        Text(text = "Some content")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun ErrorNavBarPreview() {
    AppScreenStateAware(
        scrollState = rememberScrollState(),
        uiState = UiState<Unit>(isLoading = false, error = RuntimeException()),
        retry = { },
        refresh = { },
    ) {
        Text(text = "Some content")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun ErrorNoNavBarPreview() {
    AppScreenStateAware(
        scrollState = rememberScrollState(),
        uiState = UiState<Unit>(isLoading = false, error = RuntimeException()),
        retry = { },
        refresh = { },
    ) {
        Text(text = "Some content")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewEmptyState() {
    AppScreenStateAware(
        scrollState = rememberScrollState(),
        uiState = UiState<Unit>(isLoading = false),
        isEmpty = true,
        emptyContent = {
            AppEmptyContent(
                emptyState = EmptyState(
                    titleRes = R.string.no_network_connection_error_title,
                    description = stringResource(R.string.no_network_connection_error_desc),
                    drawableRes = R.drawable.bg_no_connection
                )
            )
        },
        retry = { },
        refresh = { },
    ) {
        Text(text = "Some content")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun Preview() {
    AppScreenStateAware(
        scrollState = rememberScrollState(),
        uiState = UiState<Unit>(isLoading = false),
        retry = { },
        refresh = { },
    ) {
        Text(text = "Some content")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PaginatedPreview() {
    AppScreenStateAwarePaginatedList(
        items = flowOf(PagingData.from(listOf("Item"))).collectAsLazyPagingItems(),
        retry = { },
        refresh = { }
    ) {
        item {
            Text(text = "Some content")
        }
    }
}

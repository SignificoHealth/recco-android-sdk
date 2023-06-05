package com.shadowflight.uicommons.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import com.shadowflight.uicommons.R
import com.shadowflight.uicommons.extensions.noRippleClickable
import com.shadowflight.uicommons.extensions.setupLoadStates
import com.shadowflight.uicommons.theme.AppSpacing
import com.shadowflight.uicommons.theme.AppTheme
import kotlinx.coroutines.flow.flowOf
import java.lang.Float.min

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
fun AppScreenStateAware(
    modifier: Modifier = Modifier,
    scrollState: ScrollState? = null,
    isFloatingHeader: Boolean = false,
    isLoading: Boolean,
    throwable: Throwable? = null,
    enablePullToRefresh: Boolean = false,
    avoidClickingWhenRefreshing: Boolean = true,
    isEmpty: Boolean = false,
    retry: () -> Unit,
    colorStatusBar: Color = Color.White,
    backgroundContent: @Composable (() -> Unit)? = null,
    animatedContentShapeContent: @Composable (() -> Unit)? = null,
    animatedContent: @Composable (() -> Unit)? = null,
    emptyContent: @Composable (ColumnScope.() -> Unit)? = null,
    headerContent: @Composable ((isAnimatedContentCollapsed: Boolean) -> Unit)? = null,
    footerContent: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val isFirstLoading = remember { mutableStateOf(true) }
    val isAnimatedContentCollapsed = remember { mutableStateOf(true) }
    val isError = throwable != null

    AppTheme(colorStatusBar = colorStatusBar) {
        if (isFloatingHeader) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                backgroundContent?.invoke()

                AppScreenStateAwareContent(
                    isAnimatedContentCollapsed = isAnimatedContentCollapsed,
                    modifier = modifier,
                    isFloatingHeader = true,
                    scrollState = scrollState,
                    animatedContentShapeContent = animatedContentShapeContent,
                    animatedContent = animatedContent,
                    isLoading = isLoading,
                    isFirstLoading = isFirstLoading,
                    isEmpty = isEmpty,
                    isError = isError,
                    throwable = throwable,
                    retry = retry,
                    emptyContent = emptyContent,
                    enablePullToRefresh = enablePullToRefresh,
                    avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
                    colorStatusBar = colorStatusBar,
                    footerContent = footerContent,
                    content = content
                )

                headerContent?.let {
                    HeaderContent(
                        isFirstLoading = isFirstLoading.value || isEmpty || isError,
                        isAnimatedContentCollapsed = isAnimatedContentCollapsed.value,
                        isFloatingHeader = true,
                        content = it
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                backgroundContent?.invoke()

                Column(modifier = Modifier.fillMaxSize()) {
                    headerContent?.let {
                        HeaderContent(
                            isFirstLoading = isFirstLoading.value,
                            isAnimatedContentCollapsed = isAnimatedContentCollapsed.value,
                            isFloatingHeader = false,
                            content = it
                        )
                    }

                    AppScreenStateAwareContent(
                        isAnimatedContentCollapsed = isAnimatedContentCollapsed,
                        modifier = modifier,
                        isFloatingHeader = false,
                        scrollState = scrollState,
                        animatedContentShapeContent = animatedContentShapeContent,
                        animatedContent = animatedContent,
                        isLoading = isLoading,
                        isFirstLoading = isFirstLoading,
                        isEmpty = isEmpty,
                        isError = isError,
                        throwable = throwable,
                        retry = retry,
                        emptyContent = emptyContent,
                        enablePullToRefresh = enablePullToRefresh,
                        avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
                        colorStatusBar = colorStatusBar,
                        footerContent = footerContent,
                        content = content
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderContent(
    isFirstLoading: Boolean,
    isAnimatedContentCollapsed: Boolean,
    isFloatingHeader: Boolean,
    content: @Composable (isAnimatedContentCollapsed: Boolean) -> Unit,
) {
    if (isFirstLoading) {
        content(isAnimatedContentCollapsed = true)
    } else {
        if (isFloatingHeader) {
            AnimatedVisibility(
                visible = isAnimatedContentCollapsed,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                AppElevatedTopContent {
                    content(isAnimatedContentCollapsed = true)
                }
            }

            AnimatedVisibility(
                visible = !isAnimatedContentCollapsed,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                content(isAnimatedContentCollapsed = false)
            }
        } else {
            if (isAnimatedContentCollapsed) {
                AppElevatedTopContent {
                    content(isAnimatedContentCollapsed = true)
                }
            } else {
                content(isAnimatedContentCollapsed = false)
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
private fun AppScreenStateAwareContent(
    modifier: Modifier,
    isAnimatedContentCollapsed: MutableState<Boolean>,
    isFloatingHeader: Boolean,
    scrollState: ScrollState?,
    isLoading: Boolean,
    isFirstLoading: MutableState<Boolean>,
    isEmpty: Boolean,
    isError: Boolean,
    throwable: Throwable?,
    retry: () -> Unit,
    enablePullToRefresh: Boolean,
    avoidClickingWhenRefreshing: Boolean,
    colorStatusBar: Color,
    animatedContentShapeContent: @Composable (() -> Unit)?,
    animatedContent: @Composable (() -> Unit)?,
    emptyContent: @Composable (ColumnScope.() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit,
    footerContent: @Composable (() -> Unit)? = null,
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    swipeRefreshState.isRefreshing = isLoading
    val animatedContentHeight = remember { mutableStateOf(0f) }

    when {
        isLoading && isFirstLoading.value -> {
            HeaderAwareContent(isFloatingHeader) {
                AppProgressLoadingCircled()
            }
        }
        isError -> {
            HeaderAwareContent(isFloatingHeader) {
                AppErrorContent(
                    throwable = throwable,
                    retry = retry,
                    scrollState = rememberScrollState()
                )
            }
        }
        isEmpty -> {
            SwipeRefreshContent(
                swipeRefreshState = swipeRefreshState,
                enablePullToRefresh = enablePullToRefresh,
                avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
                retry = retry,
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
                swipeRefreshState = swipeRefreshState,
                enablePullToRefresh = enablePullToRefresh,
                avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
                retry = retry,
                colorStatusBar = colorStatusBar,
            ) {
                Column(modifier = modifier.fillMaxSize()) {
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
                                                isAnimatedContentCollapsed.value =
                                                    alpha <= 0
                                                translationY = 0.5f * scrollState.value
                                            }
                                        }
                                ) {
                                    animatedContent?.invoke()
                                }

                                animatedContentShapeContent?.invoke()
                            }
                        }

                        content()
                    }

                    if (scrollState != null) {
                        AppElevatedBottomContent(scrollState = scrollState) {
                            footerContent?.invoke()
                        }
                    } else {
                        footerContent?.invoke()
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
    colorStatusBar: Color = Color.White,
    headerContent: @Composable (() -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        headerContent?.invoke()

        SwipeRefreshContent(
            swipeRefreshState = swipeRefreshState,
            enablePullToRefresh = enablePullToRefresh,
            avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
            retry = retry,
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
    retry: () -> Unit,
    colorStatusBar: Color = Color.White,
    headerContent: @Composable (() -> Unit)? = null,
    content: LazyGridScope.() -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(false)

    SwipeRefreshContent(
        swipeRefreshState = swipeRefreshState,
        enablePullToRefresh = enablePullToRefresh,
        avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
        retry = retry,
        colorStatusBar = colorStatusBar
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
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
    enablePullToRefresh: Boolean,
    swipeRefreshState: SwipeRefreshState,
    retry: () -> Unit,
    avoidClickingWhenRefreshing: Boolean,
    colorStatusBar: Color,
    content: @Composable () -> Unit
) {
    AppTheme(colorStatusBar = colorStatusBar) {
        SwipeRefresh(
            swipeEnabled = enablePullToRefresh,
            state = swipeRefreshState,
            onRefresh = { retry() },
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
        isLoading = true,
        retry = { },
    ) {
        Text(text = "Some content")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun ErrorNavBarPreview() {
    AppScreenStateAware(
        scrollState = rememberScrollState(),
        isLoading = false,
        throwable = RuntimeException(),
        retry = { },
    ) {
        Text(text = "Some content")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun ErrorNoNavBarPreview() {
    AppScreenStateAware(
        scrollState = rememberScrollState(),
        isLoading = false,
        throwable = RuntimeException(),
        retry = { },
    ) {
        Text(text = "Some content")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun PreviewEmptyState() {
    AppScreenStateAware(
        scrollState = rememberScrollState(),
        isLoading = false,
        throwable = null,
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
    ) {
        Text(text = "Some content")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun Preview() {
    AppScreenStateAware(
        scrollState = rememberScrollState(),
        isLoading = false,
        throwable = null,
        retry = { },
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
    ) {
        item {
            Text(text = "Some content")
        }
    }
}

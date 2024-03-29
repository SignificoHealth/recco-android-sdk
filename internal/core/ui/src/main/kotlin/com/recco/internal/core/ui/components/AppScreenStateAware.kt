package com.recco.internal.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.extensions.applyIf
import com.recco.internal.core.ui.extensions.noRippleClickable
import com.recco.internal.core.ui.theme.AppSpacing
import com.recco.internal.core.ui.theme.AppTheme
import kotlin.math.min

data class UiState<T>(
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val data: T? = null
)

fun <T> Throwable.toUiState(): UiState<T> {
    return UiState(
        isLoading = false,
        error = this,
        data = null
    )
}

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
    contentPadding: PaddingValues = PaddingValues(0.dp),
    scrollState: ScrollState? = null,
    isFloatingHeader: Boolean = false,
    isFloatingFooter: Boolean = false,
    uiState: UiState<T>,
    enablePullToRefresh: Boolean = false,
    avoidClickingWhenRefreshing: Boolean = true,
    isEmpty: Boolean = false,
    retry: () -> Unit,
    refresh: (() -> Unit)? = null,
    shouldFillMaxSize: Boolean = true,
    loadingHeaderContent: @Composable (() -> Unit)? = null,
    backgroundContent: @Composable (() -> Unit)? = null,
    animatedContentShapeContent: @Composable (() -> Unit)? = null,
    animatedContent: @Composable ((uiStateData: T) -> Unit)? = null,
    emptyContent: @Composable (ColumnScope.() -> Unit)? = null,
    headerContent: @Composable ((uiStateData: T, isAnimatedContentCollapsed: Boolean) -> Unit)? = null,
    footerContent: @Composable ((uiStateData: T) -> Unit)? = null,
    content: @Composable ColumnScope.(uiStateData: T) -> Unit
) {
    val isFirstLoading = rememberSaveable { mutableStateOf(true) }
    val isAnimatedContentCollapsed = rememberSaveable { mutableStateOf(false) }
    val isError = uiState.error != null

    if (isFloatingHeader) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding())
        ) {
            backgroundContent?.invoke()

            AppScreenStateAwareContent(
                uiState = uiState,
                isAnimatedContentCollapsed = isAnimatedContentCollapsed,
                modifier = modifier,
                isFloatingHeader = true,
                isFloatingFooter = isFloatingFooter,
                scrollState = scrollState,
                animatedContentShapeContent = animatedContentShapeContent,
                animatedContent = animatedContent,
                isFirstLoading = isFirstLoading,
                isEmpty = isEmpty,
                retry = retry,
                refresh = refresh ?: {},
                loadingHeaderContent = loadingHeaderContent,
                emptyContent = emptyContent,
                enablePullToRefresh = enablePullToRefresh,
                avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
                footerContent = footerContent,
                content = content,
                shouldFillMaxSize = shouldFillMaxSize
            )

            headerContent?.let {
                HeaderContent(
                    isFirstLoading = isFirstLoading.value || isEmpty || isError,
                    isAnimatedContentCollapsed = isAnimatedContentCollapsed.value,
                    isFloatingHeader = true,
                    content = { isAnimatedContentCollapsed ->
                        uiState.data?.let { data ->
                            headerContent.invoke(data, isAnimatedContentCollapsed)
                        }
                    }
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
    } else {
        Box(
            modifier = Modifier
                .applyIf(shouldFillMaxSize) { fillMaxSize() }
                .padding(top = contentPadding.calculateTopPadding())
        ) {
            backgroundContent?.invoke()

            Column(modifier = Modifier.applyIf(shouldFillMaxSize) { fillMaxSize() }) {
                headerContent?.let {
                    HeaderContent(
                        isFirstLoading = isFirstLoading.value,
                        isAnimatedContentCollapsed = isAnimatedContentCollapsed.value,
                        isFloatingHeader = false,
                        content = { isAnimatedContentCollapsed ->
                            uiState.data?.let { data ->
                                headerContent.invoke(data, isAnimatedContentCollapsed)
                            }
                        }
                    )
                }

                AppScreenStateAwareContent(
                    isAnimatedContentCollapsed = isAnimatedContentCollapsed,
                    modifier = modifier,
                    isFloatingHeader = false,
                    isFloatingFooter = isFloatingFooter,
                    scrollState = scrollState,
                    animatedContentShapeContent = animatedContentShapeContent,
                    animatedContent = animatedContent,
                    isFirstLoading = isFirstLoading,
                    isEmpty = isEmpty,
                    uiState = uiState,
                    retry = retry,
                    refresh = refresh ?: {},
                    loadingHeaderContent = loadingHeaderContent,
                    emptyContent = emptyContent,
                    enablePullToRefresh = enablePullToRefresh,
                    avoidClickingWhenRefreshing = avoidClickingWhenRefreshing,
                    footerContent = footerContent,
                    shouldFillMaxSize = shouldFillMaxSize,
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

@Composable
private fun HeaderContent(
    isFirstLoading: Boolean,
    isAnimatedContentCollapsed: Boolean,
    isFloatingHeader: Boolean,
    content: @Composable (isAnimatedContentCollapsed: Boolean) -> Unit
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
    content: @Composable ColumnScope.() -> Unit
) {
    val extraHeight = remember {
        // TODO we might want calculate dynamically the header height or we can add any space
        // when setting up the screen component to adjust it to the header content.
        mutableStateOf(if (isFloatingHeader) AppSpacing.dp_40 else 0.dp)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(extraHeight.value))
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
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
    shouldFillMaxSize: Boolean,
    enablePullToRefresh: Boolean,
    avoidClickingWhenRefreshing: Boolean,
    loadingHeaderContent: @Composable (() -> Unit)? = null,
    animatedContentShapeContent: @Composable (() -> Unit)?,
    animatedContent: @Composable ((uiStateData: T) -> Unit)?,
    emptyContent: @Composable (ColumnScope.() -> Unit)?,
    content: @Composable ColumnScope.(data: T) -> Unit,
    footerContent: @Composable ((uiStateData: T) -> Unit)? = null,
    isFloatingFooter: Boolean = false
) {
    val isPullRefreshEnabled = remember { mutableStateOf(enablePullToRefresh) }

    // threshold needs to be smaller than offset so the behaviour keeps less bouncy
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        refreshingOffset = 76.dp,
        refreshThreshold = 52.dp,
        onRefresh = { refresh() }
    )

    val animatedContentHeight = rememberSaveable { mutableStateOf(0f) }

    Crossfade(
        targetState = uiState.isLoading && isFirstLoading.value,
        label = "loading_crossfade"
    ) { isInitialLoading ->
        if (isInitialLoading) {
            HeaderAwareContent(isFloatingHeader) {
                loadingHeaderContent?.invoke()
                AppProgressLoadingCircled(modifier = modifier.padding(top = AppSpacing.dp_24))
            }
            return@Crossfade
        }
        when {
            uiState.error != null -> {
                LaunchedEffect(Unit) {
                    isPullRefreshEnabled.value = false
                }

                HeaderAwareContent(isFloatingHeader) {
                    AppErrorContent(
                        throwable = uiState.error,
                        retry = retry,
                        scrollState = rememberScrollState()
                    )
                }
            }

            isEmpty -> {
                LaunchedEffect(Unit) {
                    isPullRefreshEnabled.value = false
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) { emptyContent?.invoke(this) }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .applyIf(shouldFillMaxSize) { fillMaxSize() }
                        .pullRefresh(pullRefreshState)
                ) {
                    LaunchedEffect(Unit) {
                        isFirstLoading.value = false
                        isPullRefreshEnabled.value = true
                    }

                    Column(
                        modifier = Modifier.applyIf(shouldFillMaxSize) { fillMaxSize() }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .applyIf(shouldFillMaxSize) { weight(1f) }
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
                                                val height =
                                                    coordinates.size.height.toFloat()
                                                // If there is no animated content we specify a default height
                                                // in order to allow elevate the header when scrolling
                                                animatedContentHeight.value =
                                                    if (height <= 0f) 10f else height
                                            }
                                            .graphicsLayer {
                                                val scrollValue =
                                                    scrollState.value.toFloat()
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
                                        uiState.data?.let { animatedContent?.invoke(it) }
                                    }
                                    animatedContentShapeContent?.invoke()
                                }
                            }

                            uiState.data?.let { content(it) }
                        }

                        if (!isFloatingFooter) {
                            if (scrollState != null) {
                                AppElevatedBottomContent(scrollState = scrollState) {
                                    uiState.data?.let { footerContent?.invoke(it) }
                                }
                            } else {
                                uiState.data?.let { footerContent?.invoke(it) }
                            }
                        }
                    }

                    when {
                        !isPullRefreshEnabled.value -> {}

                        avoidClickingWhenRefreshing && uiState.isLoading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .noRippleClickable { }
                            ) {
                                ReccoPullRefreshIndicator(
                                    state = pullRefreshState,
                                    modifier = Modifier.align(Alignment.TopCenter)
                                )
                            }
                        }

                        isPullRefreshEnabled.value -> {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                ReccoPullRefreshIndicator(
                                    state = pullRefreshState,
                                    modifier = Modifier.align(Alignment.TopCenter)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun LoadingPreview() {
    AppScreenStateAware(
        scrollState = rememberScrollState(),
        uiState = UiState<Unit>(isLoading = true),
        retry = { },
        refresh = { }
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
        refresh = { }
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
        refresh = { }
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
                    titleRes = R.string.recco_error_connection_title,
                    description = stringResource(R.string.recco_error_connection_body),
                    drawableRes = R.drawable.recco_ic_no_connection_static
                )
            )
        },
        retry = { },
        refresh = { }
    ) {
        Text(text = "Some content")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun Preview() {
    AppTheme {
        AppScreenStateAware(
            scrollState = rememberScrollState(),
            uiState = UiState<Unit>(isLoading = true),
            enablePullToRefresh = true,
            retry = { },
            refresh = { }
        ) {
            Text(text = "Some content")
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
private fun LoadingPreviewDark() {
    AppTheme(darkTheme = true) {
        AppScreenStateAware(
            scrollState = rememberScrollState(),
            uiState = UiState<Unit>(isLoading = true),
            retry = { },
            refresh = { }
        ) {
            Text(text = "Some content")
        }
    }
}

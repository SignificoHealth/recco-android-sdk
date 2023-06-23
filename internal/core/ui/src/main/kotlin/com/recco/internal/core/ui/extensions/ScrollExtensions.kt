package com.recco.internal.core.ui.extensions

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*


private const val SCROLLING_OFFSET = 20
private const val EDGE_REACHED_OFFSET = 5

/**
 * Detect if it moves up the screen
 */
@Composable
fun ScrollState.isScrollingUp(scrollOffset: Int = SCROLLING_OFFSET): Boolean {
    val lastScroll = remember { mutableStateOf(0) }

    if (this.value > lastScroll.value + scrollOffset) {
        lastScroll.value = this.value - scrollOffset // scroll up
    } else if (this.value < lastScroll.value - scrollOffset) {
        lastScroll.value = this.value + scrollOffset // scroll down
    }

    val isInitialStateORScrollingUp = remember {
        derivedStateOf {
            when {
                this.value >= lastScroll.value + scrollOffset -> false
                this.value < lastScroll.value -> true
                else -> lastScroll.value - scrollOffset < 0 // initial state
            }
        }
    }
    return isInitialStateORScrollingUp.value
}

/**
 * Detect if it moves up the screen
 */
@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

/**
 * Detect if the scroll has reached the bottom
 */
@Composable
fun ScrollState.isEndReached(): Boolean {
    val endReached by remember {
        derivedStateOf {
            if (this.maxValue == Int.MAX_VALUE) {
                true
            } else {
                this.value >= this.maxValue - EDGE_REACHED_OFFSET
            }
        }
    }
    return endReached
}

/**
 * Detect if the scroll has reached the top
 */
fun  ScrollState.isTopReached(): Boolean {
    return this.value < EDGE_REACHED_OFFSET
}

/**
 * Detect if the scroll has reached the top
 */
fun LazyListState.isTopReached(): Boolean {
    return this.firstVisibleItemScrollOffset < EDGE_REACHED_OFFSET
}

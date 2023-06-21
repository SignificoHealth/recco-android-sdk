package com.shadowflight.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ui.Scaffold
import com.shadowflight.core.ui.components.AppScreenStateAware
import com.shadowflight.core.ui.components.AppTopBar
import com.shadowflight.core.ui.theme.AppTheme
import com.shadowflight.ui.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

private const val ANIM_DURATION = 1000
private const val TOTAL_DOTS = 4
private const val START_DELAY_DOT = (ANIM_DURATION + (ANIM_DURATION / 1.5f)).toInt() / TOTAL_DOTS

// Starting from the lowest number, each number represent a dot in the screen.
private val DOTS_ANIM_SEQUENCE = listOf(
    0, 1,
    3, 2
)

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val uiState by viewModel.viewState.collectAsStateWithLifecycle()
            AppTheme {
                val user = uiState.data?.user
                if (user != null) {
                    AppNavHost(
                        user = user,
                        navController = rememberNavController()
                    )
                } else {
                    Scaffold(
                        topBar = {
                            if (uiState.error != null) {
                                AppTopBar()
                            }
                        },
                        backgroundColor = AppTheme.colors.background,
                        contentPadding = WindowInsets.navigationBars.asPaddingValues()
                    ) { innerPadding ->
                        AppScreenStateAware(
                            contentPadding = innerPadding,
                            uiState = uiState,
                            retry = { viewModel.onUserInteract(MainUserInteract.Retry) },
                            isEmpty = true,
                            emptyContent = { LoadingContent() }
                        ) {}
                    }
                }
            }
        }
    }

    @Composable
    private fun ColumnScope.LoadingContent() {
        Box(
            modifier = Modifier.Companion.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                (0 until TOTAL_DOTS step 2).forEach {
                    Row {
                        AnimatedDotIcon(delay = START_DELAY_DOT * DOTS_ANIM_SEQUENCE[it])
                        AnimatedDotIcon(delay = START_DELAY_DOT * DOTS_ANIM_SEQUENCE[it + 1])
                    }
                }
            }
        }
    }

    @Composable
    private fun AnimatedDotIcon(delay: Int) {
        val infiniteTransition = rememberInfiniteTransition()
        val color by infiniteTransition.animateColor(
            initialValue = AppTheme.colors.illustration20,
            targetValue = AppTheme.colors.illustration,
            animationSpec = infiniteRepeatable(
                initialStartOffset = StartOffset(
                    offsetMillis = delay,
                    offsetType = StartOffsetType.Delay
                ),
                animation = tween(
                    durationMillis = ANIM_DURATION,
                    easing = FastOutLinearInEasing
                ),
                repeatMode = RepeatMode.Reverse,
            )
        )

        Box(
            modifier = Modifier
                .padding(4.dp)
                .size(28.dp)
                .clip(CircleShape)
                .background(color)
        )
    }
}

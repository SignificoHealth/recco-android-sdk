package com.shadowflight.core.questionnaire

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.insets.ui.Scaffold
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.model.questionnaire.MultiChoiceQuestion
import com.shadowflight.core.model.questionnaire.NumericQuestion
import com.shadowflight.core.questionnaire.QuestionnaireUserInteract.*
import com.shadowflight.core.questionnaire.QuestionnaireViewEvent.*
import com.shadowflight.core.questionnaire.numeric.NumericInput
import com.shadowflight.core.ui.R
import com.shadowflight.core.ui.components.AppLinearProgress
import com.shadowflight.core.questionnaire.multichoice.MultiChoiceInput
import com.shadowflight.core.ui.components.AppScreenStateAware
import com.shadowflight.core.ui.components.AppTopBar
import com.shadowflight.core.ui.components.BackIconButton
import com.shadowflight.core.ui.components.UiState
import com.shadowflight.core.ui.components.appButtonHeight
import com.shadowflight.core.ui.extensions.asTitle
import com.shadowflight.core.ui.theme.AppSpacing
import com.shadowflight.core.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

@Composable
internal fun QuestionnaireRoute(
    topic: Topic,
    navigateUp: () -> Unit,
    viewModel: QuestionnaireViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle(
        initialValue = UiState()
    )
    QuestionnaireScreen(
        topic = topic,
        uiState = uiState,
        onUserInteract = { viewModel.onUserInteract(it) },
        navigateUp = navigateUp,
        viewEvents = viewModel.viewEvents
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestionnaireScreen(
    topic: Topic,
    uiState: UiState<QuestionnaireUI>,
    viewEvents: Flow<QuestionnaireViewEvent>,
    onUserInteract: (QuestionnaireUserInteract) -> Unit,
    navigateUp: () -> Unit,
    contentPadding: PaddingValues = WindowInsets.navigationBars.asPaddingValues(),
) {
    val pagerState = rememberPagerState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit) {
        viewEvents.collectLatest { event ->
            when (event) {
                QuestionnaireSubmitted -> navigateUp()
                is ScrollTo -> {
                    focusManager.clearFocus()
                    pagerState.scrollToPage(event.page)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = topic.asTitle(),
                elevation = 0.dp,
                navigationIcon = {
                    BackIconButton(onClick = navigateUp)
                }
            )
        },
        backgroundColor = AppTheme.colors.background,
        contentPadding = contentPadding
    ) { innerPadding ->
        AppScreenStateAware(
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
            uiState = uiState,
            retry = { onUserInteract(Retry) },
            footerContent = { data ->
                Surface(
                    modifier = Modifier.zIndex(8f),
                    elevation = 8.dp
                ) {
                    QuestionnaireFooterContent(
                        modifier = Modifier
                            .background(AppTheme.colors.background)
                            .fillMaxWidth()
                            .padding(vertical = AppSpacing.dp_24, horizontal = AppSpacing.dp_24),
                        showBack = data.showBack,
                        nextEnabled = data.isNextEnabled,
                        isLastPage = data.isLastPage,
                        isQuestionnaireSubmitLoading = data.isQuestionnaireSubmitLoading,
                        onBackClicked = { onUserInteract(BackClicked) },
                        onNextClicked = { onUserInteract(NextClicked) })
                }
            },
            isFloatingFooter = true
        ) { data ->
            QuestionnaireContent(
                data = data,
                state = pagerState,
                navigateUp = navigateUp,
                onUserInteract = onUserInteract
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun QuestionnaireContent(
    navigateUp: () -> Unit,
    data: QuestionnaireUI,
    state: PagerState,
    onUserInteract: (QuestionnaireUserInteract) -> Unit
) {
    BackHandler(onBack = {
        if (data.isFirstPage) {
            navigateUp()
        } else {
            onUserInteract(BackClicked)
        }
    })

    val questions = data.questionnaire.questions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.colors.background)
    ) {
        Column(
            Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppLinearProgress(
                modifier = Modifier
                    .padding(horizontal = AppSpacing.dp_24),
                progress = data.progress,
                shape = CircleShape,
                animDuration = 250
            )
            Spacer(Modifier.height(AppSpacing.dp_8))

            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(color = AppTheme.colors.accent)) {
                    append((data.currentPage + 1).toString())
                }
                withStyle(style = SpanStyle(color = AppTheme.colors.primary)) {
                    append("/${data.questionnaire.questions.size}")
                }
            }, style = AppTheme.typography.h4)
            Spacer(Modifier.height(AppSpacing.dp_24))
        }

        Divider(
            color = AppTheme.colors.primary20,
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
        )

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            pageCount = questions.size,
            state = state,
            userScrollEnabled = false
        ) { page ->
            val question = questions[page]

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = AppSpacing.dp_24)
                    .fillMaxSize()
            ) {
                Spacer(Modifier.height(AppSpacing.dp_32))
                Text(text = question.text, style = AppTheme.typography.cta)

                when (question) {
                    is MultiChoiceQuestion -> {
                        if (!question.isSingleChoice) {
                            Spacer(Modifier.height(AppSpacing.dp_16))
                            Text(
                                text = stringResource(R.string.multiple_answers_possible),
                                style = AppTheme.typography.body3.copy(color = AppTheme.colors.primary20)
                            )
                        }
                        Spacer(Modifier.height(AppSpacing.dp_32))

                        question.options.forEach { option ->
                            MultiChoiceInput(
                                selected = option.isSelected,
                                text = option.text,
                                singleChoice = question.isSingleChoice,
                                onClick = {
                                    onUserInteract(
                                        ClickOnMultiChoiceAnswerOption(
                                            question = question,
                                            option = option
                                        )
                                    )
                                }
                            )
                            Spacer(Modifier.height(AppSpacing.dp_8))
                        }
                    }

                    is NumericQuestion -> {
                        Spacer(Modifier.height(AppSpacing.dp_32))
                        NumericInput(
                            onValueChange = {
                                onUserInteract(
                                    WriteOnNumericQuestion(
                                        question = question,
                                        value = it
                                    )
                                )
                            },
                            format = question.format,
                            maxValue = question.maxValue
                        )
                    }
                }

                Spacer(Modifier.height(AppSpacing.dp_24 * 2 + appButtonHeight))
            }
        }
    }
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(QuestionnaireUIStatePreviewProvider::class) uiState: UiState<QuestionnaireUI>
) {
    QuestionnaireScreen(
        topic = Topic.NUTRITION,
        uiState = uiState,
        navigateUp = {},
        onUserInteract = {},
        viewEvents = flow { }
    )
}


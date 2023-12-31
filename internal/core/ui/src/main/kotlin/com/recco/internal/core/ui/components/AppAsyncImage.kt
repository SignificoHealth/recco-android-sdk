package com.recco.internal.core.ui.components

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.extensions.dpToPx
import com.recco.internal.core.ui.extensions.pxToDp

// width/height positive ratio
const val ASPECT_RATIO_1_1 = 1f
const val ASPECT_RATIO_6_5 = 6f / 5f // 0.83
const val ASPECT_RATIO_4_3 = 4f / 3f // 0.75
const val ASPECT_RATIO_10_7 = 10f / 7f // 0.7
const val ASPECT_RATIO_16_9 = 16f / 9f // 0.56
const val ASPECT_RATIO_10_5 = 10f / 5f // 0.5
const val ASPECT_RATIO_10_4 = 10f / 4f // 0.4
const val MAX_SERVER_SIZE = 1080

/**
 * @param data This can be a URL, a [painterResource], a [java.io.File], ...
 * @param placeholderRes Used for error and fallback states if [placeholderContent] is null. Not use both.
 * @param placeholderContent Used for error and fallback states if [placeholderRes] is null. Not use both.
 * @param loadingAnimationDrawable Used for loading state if [loadingContent] is null. Not use both.
 * @param loadingContent Used for loading state if [loadingAnimationDrawable] is null. Not use both.
 * @param contentScale
 * @param onStateChange Useful for example if you need to get for example the size of the image loaded.
 */
@Composable
fun AppAsyncImage(
    modifier: Modifier,
    data: Any?,
    alt: String? = null,
    @DrawableRes placeholderRes: Int? = R.drawable.recco_bg_image_placeholder,
    placeholderContent: (@Composable () -> Unit)? = null,
    loadingAnimationDrawable: AnimationDrawable? = loadingAnimationDrawable(),
    loadingContent: (@Composable () -> Unit)? = null,
    contentScale: ContentScale = ContentScale.Crop,
    onStateChange: (AsyncImagePainter.State) -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier) {
        if (data == null && placeholderContent != null) {
            placeholderContent()
        } else {
            val placeholderPainter = placeholderRes?.let { painterResource(it) }
                .takeIf { placeholderContent == null }

            val model = if (data is String) {
                constructDynamicImageUrl(
                    url = data,
                    viewWidthPx = constraints.maxWidth,
                    viewHeightPx = constraints.maxHeight
                )
            } else {
                data
            }

            val painter = rememberAsyncImagePainter(
                model = model,
                contentScale = contentScale,
                error = placeholderPainter,
                fallback = placeholderPainter
            )

            val state = painter.state
            onStateChange(state)

            Image(
                painter = painter,
                contentScale = contentScale,
                contentDescription = alt,
                modifier = Modifier.fillMaxSize()
            )

            when (state) {
                AsyncImagePainter.State.Empty -> Unit
                is AsyncImagePainter.State.Loading -> {
                    if (loadingAnimationDrawable == null) {
                        loadingContent?.invoke()
                    } else {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = rememberDrawablePainter(loadingAnimationDrawable),
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    }
                }

                is AsyncImagePainter.State.Success -> Unit
                is AsyncImagePainter.State.Error -> {
                    if (placeholderRes == null) {
                        placeholderContent?.invoke()
                    }
                }
            }
        }
    }
}

@Composable
fun loadingAnimationDrawable() = loadingAnimationDrawable(
    context = LocalContext.current,
    loadingResImages = listOf(
        R.drawable.recco_bg_loading_1,
        R.drawable.recco_bg_loading_2,
        R.drawable.recco_bg_loading_3,
        R.drawable.recco_bg_loading_4,
        R.drawable.recco_bg_loading_5,
        R.drawable.recco_bg_loading_6,
        R.drawable.recco_bg_loading_7
    )
)

@Composable
fun loadingCardAnimationDrawable() = loadingAnimationDrawable(
    context = LocalContext.current,
    loadingResImages = listOf(
        R.drawable.recco_bg_loading_card_1,
        R.drawable.recco_bg_loading_card_2,
        R.drawable.recco_bg_loading_card_3,
        R.drawable.recco_bg_loading_card_4,
        R.drawable.recco_bg_loading_card_5,
        R.drawable.recco_bg_loading_card_6,
        R.drawable.recco_bg_loading_card_7
    )
)

private fun loadingAnimationDrawable(
    context: Context,
    loadingResImages: List<Int>
): AnimationDrawable {
    val animationDrawable = AnimationDrawable()
    val durationTransition = (400..1000).random()

    loadingResImages.shuffled().forEach { imageRes ->
        ContextCompat.getDrawable(context, imageRes)?.let { drawable ->
            animationDrawable.addFrame(drawable, durationTransition)
            animationDrawable.setEnterFadeDuration(durationTransition)
            animationDrawable.setExitFadeDuration(durationTransition)
        }
    }

    return animationDrawable
}

@Composable
private fun constructDynamicImageUrl(
    url: String,
    viewWidthPx: Int,
    viewHeightPx: Int
): String {
    val standardWidth = normalize(viewWidthPx.pxToDp())
    val standardHeight = normalize(viewHeightPx.pxToDp())
    val quality = 70
    val format = "webp"
    val fit = "cover"

    return url +
        "?width=$standardWidth" +
        "&height=$standardHeight" +
        "&quality=$quality" +
        "&format=$format" +
        "&fit=$fit"
}

@Composable
private fun normalize(value: Dp) = when {
    value <= 100.dp -> 100.dp
    value <= 200.dp -> 200.dp
    value <= 300.dp -> 300.dp
    value <= 400.dp -> 400.dp
    value <= 500.dp -> 500.dp
    value <= 600.dp -> 600.dp
    value <= 700.dp -> 700.dp
    value <= 800.dp -> 800.dp
    value <= 900.dp -> 900.dp
    else -> MAX_SERVER_SIZE.dp
}.dpToPx()

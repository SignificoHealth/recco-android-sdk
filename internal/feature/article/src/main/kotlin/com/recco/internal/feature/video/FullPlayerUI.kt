package com.recco.internal.feature.video

import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.media.Video

sealed class FullPlayerUI(val mediaUrl: String) {
    class VideoUi(video: Video) : FullPlayerUI(video.videoUrl)
    class AudioUi(audio: Audio) : FullPlayerUI(audio.audioUrl)
}

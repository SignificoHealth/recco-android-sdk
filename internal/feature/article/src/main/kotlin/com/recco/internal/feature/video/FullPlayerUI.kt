package com.recco.internal.feature.video

import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.media.Video

sealed class FullPlayerUI(val mediaUrl: String) {
    class VideoUi(val video: Video) : FullPlayerUI(video.videoUrl)
    class AudioUi(val audio: Audio) : FullPlayerUI(audio.audioUrl)
}

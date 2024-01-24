package com.recco.internal.feature.media.player

import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.media.Video

sealed class FullMediaPlayerUI(val mediaUrl: String) {
    class VideoUi(val video: Video) : FullMediaPlayerUI(video.videoUrl)
    class AudioUi(val audio: Audio) : FullMediaPlayerUI(audio.audioUrl)
}

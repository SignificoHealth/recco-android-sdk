package com.recco.internal.feature.video

import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.media.Video

sealed class FullPlayerUI(val mediaUrl: String) {
    class FullVideoPlayerUI(video: Video) : FullPlayerUI(video.videoUrl)
    class FullAudioPlayerUI(audio: Audio) : FullPlayerUI(audio.audioUrl)
}

@file:OptIn(UnstableApi::class) package com.recco.internal.core.ui.notifications

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.OptIn
import androidx.core.graphics.drawable.toBitmap
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerNotificationManager
import coil.imageLoader
import coil.request.ImageRequest
import com.google.common.util.concurrent.ListenableFuture
import com.recco.internal.core.ui.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val NOW_PLAYING_CHANNEL_ID = "com.recco.internal.NOW_PLAYING"
const val NOW_PLAYING_NOTIFICATION_ID = 0xb339 // Arbitrary number used to identify our notification
const val NOTIFICATION_LARGE_ICON_SIZE = 144 // px

class MediaNotificationManager(
    private val context: Context,
    sessionToken: SessionToken,
    private val player: Player,
    notificationListener: PlayerNotificationManager.NotificationListener
) {
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaController.Builder(context, sessionToken)
            .buildAsync()

        notificationManager = PlayerNotificationManager.Builder(
            context,
            NOW_PLAYING_NOTIFICATION_ID,
            NOW_PLAYING_CHANNEL_ID
        )
            .setChannelNameResourceId(R.string.recco_media_channel_name)
            .setChannelDescriptionResourceId(R.string.recco_media_channel_description)
            .setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
            .setNotificationListener(notificationListener)
            .setSmallIconResourceId(R.drawable.recco_ic_play)
            .build()
            .apply {
                setUseRewindAction(true)
                setUseFastForwardAction(true)
            }
    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotificationForPlayer(player: Player){
        notificationManager.setPlayer(player)
    }

    @UnstableApi private inner class DescriptionAdapter(private val controller: ListenableFuture<MediaController>) :
        PlayerNotificationManager.MediaDescriptionAdapter {

        var currentIconUri: Uri? = null
        var currentBitmap: Bitmap? = null

        override fun createCurrentContentIntent(player: Player): PendingIntent? =
            controller.get().sessionActivity

        override fun getCurrentContentText(player: Player) =
            ""

        override fun getCurrentContentTitle(player: Player) =
            controller.get().mediaMetadata.title.toString()

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            val iconUri = controller.get().mediaMetadata.artworkUri
            return if (currentIconUri != iconUri || currentBitmap == null) {

                // Cache the bitmap for the current song so that successive calls to
                // `getCurrentLargeIcon` don't cause the bitmap to be recreated.
                currentIconUri = iconUri
                serviceScope.launch {
                    currentBitmap = iconUri?.let {
                        resolveUriAsBitmap(it)
                    }
                    currentBitmap?.let { callback.onBitmap(it) }
                }
                null
            } else {
                currentBitmap
            }
        }

        private suspend fun resolveUriAsBitmap(uri: Uri): Bitmap? {
            return withContext(Dispatchers.IO) {
                context.imageLoader.execute(
                    ImageRequest.Builder(context)
                    .data(uri)
                    .size(NOTIFICATION_LARGE_ICON_SIZE)
                    .build()).drawable?.toBitmap()
            }
        }
    }
}

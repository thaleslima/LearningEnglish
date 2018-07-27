package net.atebtech.english.media

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.DataSource
import net.atebtech.english.media.extensions.*
import java.util.concurrent.TimeUnit

class UampPlaybackPreparer(
        private val exoPlayer: ExoPlayer,
        private val dataSourceFactory: DataSource.Factory
) : MediaSessionConnector.PlaybackPreparer {

    /**
     * UAMP supports preparing (and playing) from search, as well as media ID, so those
     * capabilities are declared here.
     *
     * TODO: Add support for ACTION_PREPARE and ACTION_PLAY, which mean "prepare/play something".
     */
    override fun getSupportedPrepareActions(): Long =
            PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                    PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH

    override fun onPrepare() = Unit

    /**
     * Handles callbacks to both [MediaSessionCompat.Callback.onPrepareFromMediaId]
     * *AND* [MediaSessionCompat.Callback.onPlayFromMediaId] when using [MediaSessionConnector].
     * This is done with the expectation that "play" is just "prepare" + "play".
     *
     * If your app needs to do something special for either 'prepare' or 'play', it's possible
     * to check [ExoPlayer.getPlayWhenReady]. If this returns `true`, then it's
     * [MediaSessionCompat.Callback.onPlayFromMediaId], otherwise it's
     * [MediaSessionCompat.Callback.onPrepareFromMediaId].
     */
    override fun onPrepareFromMediaId(mediaId: String?, extras: Bundle?) {
        extras?.let {
            val song = it.getParcelable(EXTRA_MEDIA) as Media?
            if (song == null) {
                Log.w(TAG, "Content not found: MediaID=$mediaId")
            } else {
                val mediaMedia = MediaMetadataCompat.Builder()
                        .from(song)
                        .build()

                val mediaMediaList = ArrayList<MediaMetadataCompat>()
                mediaMediaList.add(mediaMedia)
                val mediaSource = mediaMediaList.toMediaSource(dataSourceFactory)
                exoPlayer.prepare(mediaSource)
            }
        }
    }


    fun MediaMetadataCompat.Builder.from(media: Media): MediaMetadataCompat.Builder {
        // The duration from the JSON is given in seconds, but the rest of the code works in
        // milliseconds. Here's where we convert to the proper units.
        val durationMs = TimeUnit.SECONDS.toMillis(media.duration)

        id = media.id
        title = media.title
        artist = media.artist
        album = media.album
        duration = durationMs
        genre = media.genre
        mediaUri = media.source
        albumArtUri = media.image
        trackNumber = media.trackNumber
        trackCount = media.totalTrackCount
        flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE

        // To make things easier for *displaying* these, set the display properties as well.
        displayTitle = media.title
        displaySubtitle = media.artist
        displayDescription = media.album
        displayIconUri = media.image

        // Add downloadStatus to force the creation of an "extras" bundle in the resulting
        // MediaMetadataCompat object. This is needed to send accurate metadata to the
        // media session during updates.
        downloadStatus = MediaDescriptionCompat.STATUS_NOT_DOWNLOADED

        // Allow it to be used in the typical builder style.
        return this
    }

    /**
     * Handles callbacks to both [MediaSessionCompat.Callback.onPrepareFromSearch]
     * *AND* [MediaSessionCompat.Callback.onPlayFromSearch] when using [MediaSessionConnector].
     * (See above for details.)
     *
     * This method is used by the Google Assistant to respond to requests such as:
     * - Play Geisha from Wake Up on UAMP
     * - Play electronic music on UAMP
     * - Play music on UAMP
     *
     * For details on how search is handled, see [.search].
     */
    override fun onPrepareFromSearch(query: String?, extras: Bundle?) {
//        musicSource.whenReady {
//            val metadataList = musicSource.search(query ?: "", extras ?: Bundle.EMPTY)
//            if (metadataList.isNotEmpty()) {
//                val mediaSource = metadataList.toMediaSource(dataSourceFactory)
//                exoPlayer.prepare(mediaSource)
//            }
//        }
    }

    override fun onPrepareFromUri(uri: Uri?, extras: Bundle?) = Unit

    override fun getCommands(): Array<String>? = null

    override fun onCommand(
            player: Player?,
            command: String?,
            extras: Bundle?,
            cb: ResultReceiver?
    ) = Unit

    companion object {
        const val EXTRA_MEDIA = "extra_media"
    }
}

private const val TAG = "MediaSessionHelper"

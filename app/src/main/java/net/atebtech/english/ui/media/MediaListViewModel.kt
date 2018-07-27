package net.atebtech.english.ui.media

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.*
import net.atebtech.english.EMPTY_PLAYBACK_STATE
import net.atebtech.english.MediaSessionConnection
import net.atebtech.english.NOTHING_PLAYING
import net.atebtech.english.data.SongRepository
import net.atebtech.english.media.extensions.id
import net.atebtech.english.media.extensions.isPlayEnabled
import net.atebtech.english.media.extensions.isPlaying
import net.atebtech.english.media.extensions.isPrepared
import net.atebtech.english.model.Song

class SongListViewModel internal constructor(repository: SongRepository,
                                             mediaSessionConnection: MediaSessionConnection) : ViewModel() {
    private var list: LiveData<List<Song>> = repository.getSongs()

    private val _mediaItems = MutableLiveData<CurrentSong>().apply { postValue(CurrentSong()) }
    private val mediaItems: LiveData<CurrentSong> = _mediaItems

    fun getSongs(): LiveData<List<Song>> {
        return list
    }

    fun getCurrentSong(): LiveData<CurrentSong> {
        return mediaItems
    }

    fun mediaItemClicked(it: Song) {
        val nowPlaying = mediaSessionConnection.nowPlaying.value
        val transportControls = mediaSessionConnection.transportControls

        val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared && it.id == nowPlaying?.id) {
            mediaSessionConnection.playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> transportControls.pause()
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Log.w(TAG, "Playable item clicked but neither play nor pause are enabled!" +
                                " (mediaId=${it.id})")
                    }
                }
            }
        } else {
            transportControls.playFromMediaId(it.id, it.getBundle())
        }
    }

    private val playbackStateObserver = Observer<PlaybackStateCompat> {
        val playbackState = it ?: EMPTY_PLAYBACK_STATE
        val metadata = mediaSessionConnection.nowPlaying.value ?: NOTHING_PLAYING
        _mediaItems.postValue(updateState(playbackState, metadata))
    }

    private val mediaMetadataObserver = Observer<MediaMetadataCompat> {
        val playbackState = mediaSessionConnection.playbackState.value ?: EMPTY_PLAYBACK_STATE
        val metadata = it ?: NOTHING_PLAYING
        _mediaItems.postValue(updateState(playbackState, metadata))
    }

    private val mediaSessionConnection = mediaSessionConnection.also {
        it.playbackState.observeForever(playbackStateObserver)
        it.nowPlaying.observeForever(mediaMetadataObserver)
    }

    override fun onCleared() {
        super.onCleared()

        // Remove the permanent observers from the MediaSessionConnection.
        mediaSessionConnection.playbackState.removeObserver(playbackStateObserver)
        mediaSessionConnection.nowPlaying.removeObserver(mediaMetadataObserver)
    }

    private fun updateState(playbackState: PlaybackStateCompat,
                            mediaMetadata: MediaMetadataCompat): CurrentSong {
        return CurrentSong(mediaMetadata.id, playbackState)
    }

    class Factory(private val repository: SongRepository,
                  private val mediaSessionConnection: MediaSessionConnection) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SongListViewModel(repository, mediaSessionConnection) as T
        }
    }

    class CurrentSong(val id: String? = null, val playbackState: PlaybackStateCompat? = null)
}

private const val TAG = "SongListViewModel"

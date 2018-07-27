package net.atebtech.english.ui.main

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.*
import net.atebtech.english.EMPTY_PLAYBACK_STATE
import net.atebtech.english.MediaSessionConnection
import net.atebtech.english.NOTHING_PLAYING
import net.atebtech.english.data.SongRepository
import net.atebtech.english.media.extensions.*
import net.atebtech.english.model.Song

class MainViewModel internal constructor(private val repository: SongRepository,
                                         mediaSessionConnection: MediaSessionConnection) : ViewModel() {
    private val _mediaItems = MutableLiveData<CurrentSong>().apply { postValue(CurrentSong()) }
    private val mediaItems: LiveData<CurrentSong> = _mediaItems

    fun getCurrentSong(): LiveData<CurrentSong> {
        return mediaItems
    }

    fun mediaItemClicked(id: String) {
        //val nowPlaying = mediaSessionConnection.nowPlaying.value
        val transportControls = mediaSessionConnection.transportControls

        val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared) {
            mediaSessionConnection.playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> transportControls.pause()
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Log.w(TAG, "Playable item clicked but neither play nor pause are enabled!")
                    }
                }
            }
        } else {
            playFromMediaId(id)
        }
    }

    private fun playFromMediaId(id: String) {
        val transportControls = mediaSessionConnection.transportControls

        val myLiveData = repository.getSongById(id)
        myLiveData.observeForever(object : Observer<Song?> {
            override fun onChanged(it: Song?) {
                it?.let {
                    transportControls.playFromMediaId(it.id, it.getBundle())
                }
                myLiveData.removeObserver(this)
            }
        })
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

        val isPlaying = playbackState.isPlaying ?: false
        val show = !TextUtils.isEmpty(mediaMetadata.id)

        return CurrentSong(mediaMetadata.id, mediaMetadata.title, mediaMetadata.album, isPlaying, show)
    }

    class Factory(private val repository: SongRepository,
                  private val mediaSessionConnection: MediaSessionConnection) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(repository, mediaSessionConnection) as T
        }
    }

    class CurrentSong(val id: String? = null,
                      val title: String? = null,
                      val album: String? = null,
                      val play: Boolean = false,
                      val show: Boolean = false)
}

private const val TAG = "SongViewModel"

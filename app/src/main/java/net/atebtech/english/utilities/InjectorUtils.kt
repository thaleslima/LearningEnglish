package net.atebtech.english.utilities

import android.content.ComponentName
import android.content.Context
import net.atebtech.english.MediaSessionConnection
import net.atebtech.english.data.AppDatabase
import net.atebtech.english.data.SongRepository
import net.atebtech.english.media.MusicService
import net.atebtech.english.ui.category.SongListViewModel

object InjectorUtils {
    private fun provideMediaSessionConnection(context: Context): MediaSessionConnection {
        return MediaSessionConnection.getInstance(context,
                ComponentName(context, MusicService::class.java))
    }

    private fun getPlantRepository(context: Context): SongRepository {
        return SongRepository.getInstance(AppDatabase.getInstance(context).songDao())
    }

    fun provideSongListViewModelFactory(context: Context): SongListViewModel.Factory {
        val applicationContext = context.applicationContext
        val mediaSessionConnection = provideMediaSessionConnection(applicationContext)
        val repository = getPlantRepository(applicationContext)
        return SongListViewModel.Factory(repository, mediaSessionConnection)
    }
}
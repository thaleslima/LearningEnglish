package net.atebtech.english.model

import android.os.Bundle
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.atebtech.english.media.Media
import net.atebtech.english.media.UampPlaybackPreparer
import java.util.ArrayList

@Entity(tableName = "song")
data class Song(
        @PrimaryKey @ColumnInfo(name = "id")
        var id: String = "",
        var idCategory: Long = 0L,
        var category: String? = null,
        var title: String? = null,
        var album: String? = null,
        var artist: String? = null,
        var genre: String? = null,
        var source: String? = null,
        var image: String? = null,
        var trackNumber: Long = -1,
        var totalTrackCount: Long = -1,
        var duration: Long = -1,
        var site: String? = null) {

    private fun mapperTo(): Media {
        val media = Media()
        media.id = id
        media.idCategory = idCategory
        media.category = category
        media.title = title
        media.album = album
        media.artist = artist
        media.genre = genre
        media.source = source
        media.image = image
        media.trackNumber = trackNumber
        media.duration = duration
        media.site = site
        return media
    }

    fun getBundle(): Bundle? {
        val bundle = Bundle()
        bundle.putParcelable(UampPlaybackPreparer.EXTRA_MEDIA, mapperTo())
        return bundle
    }

    companion object {
        val ITEMS: MutableList<Song> = ArrayList()

        private const val COUNT = 25

        init {
            for (i in 1..COUNT) {
                ITEMS.add(Song())
            }
        }
    }
}


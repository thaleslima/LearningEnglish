package net.atebtech.english.media

import android.os.Parcel
import android.os.Parcelable

data class Media (
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
        var site: String? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeLong(idCategory)
        parcel.writeString(category)
        parcel.writeString(title)
        parcel.writeString(album)
        parcel.writeString(artist)
        parcel.writeString(genre)
        parcel.writeString(source)
        parcel.writeString(image)
        parcel.writeLong(trackNumber)
        parcel.writeLong(totalTrackCount)
        parcel.writeLong(duration)
        parcel.writeString(site)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Media> {
        override fun createFromParcel(parcel: Parcel): Media {
            return Media(parcel)
        }

        override fun newArray(size: Int): Array<Media?> {
            return arrayOfNulls(size)
        }
    }
}
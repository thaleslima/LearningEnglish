package net.atebtech.english.model

import java.util.ArrayList

data class Song(
        var id: Long = 0L,

        var idCategory: Long = 0L,

        var description: String? = null) {

    companion object {
        val ITEMS: MutableList<Song> = ArrayList()

        private val COUNT = 25

        init {
            for (i in 1..COUNT) {
                ITEMS.add(Song())
            }
        }
    }
}


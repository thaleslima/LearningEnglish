package net.atebtech.english.data

class SongRepository private constructor(private val plantDao: SongDao) {
    fun getSongs() = plantDao.getSongs()

    fun getSongById(id: String) = plantDao.getSongById(id)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: SongRepository? = null

        fun getInstance(plantDao: SongDao) =
                instance ?: synchronized(this) {
                    instance ?: SongRepository(plantDao).also { instance = it }
                }
    }
}

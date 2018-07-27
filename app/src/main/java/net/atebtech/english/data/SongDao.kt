package net.atebtech.english.data


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.atebtech.english.model.Song

@Dao
interface SongDao {
    @Query("SELECT * FROM song")
    fun getSongs(): LiveData<List<Song>>

    @Query("SELECT * FROM song WHERE id = :id")
    fun getSongById(id: String): LiveData<Song>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(plants: List<Song>)
}
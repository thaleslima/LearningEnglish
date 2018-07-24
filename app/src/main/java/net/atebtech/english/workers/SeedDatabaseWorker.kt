package net.atebtech.english.workers

import android.util.Log
import androidx.work.Worker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import net.atebtech.english.data.AppDatabase
import net.atebtech.english.model.Song
import net.atebtech.english.utilities.DATA_FILENAME

class SeedDatabaseWorker : Worker() {
    private val TAG = SeedDatabaseWorker::class.java.simpleName

    override fun doWork(): Worker.Result {
        val plantType = object : TypeToken<List<Song>>() {}.type
        var jsonReader: JsonReader? = null

        return try {
            val inputStream = applicationContext.assets.open(DATA_FILENAME)
            jsonReader = JsonReader(inputStream.reader())
            val plantList: List<Song> = Gson().fromJson(jsonReader, plantType)
            val database = AppDatabase.getInstance(applicationContext)
            database.songDao().insertAll(plantList)
            Worker.Result.SUCCESS
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Worker.Result.FAILURE
        } finally {
            jsonReader?.close()
        }
    }
}
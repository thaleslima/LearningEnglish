package net.atebtech.english.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "category")
data class Category(

        @PrimaryKey @ColumnInfo(name = "id")
        var id: Long = 0L,

        var description: String? = null) {

    companion object {
        val ITEMS: MutableList<Category> = ArrayList()

        private val COUNT = 25

        init {
            for (i in 1..COUNT) {
                ITEMS.add(Category())
            }
        }
    }
}

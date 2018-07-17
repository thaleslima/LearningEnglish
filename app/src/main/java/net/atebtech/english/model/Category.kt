package net.atebtech.english.model

import java.util.*

data class Category(
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

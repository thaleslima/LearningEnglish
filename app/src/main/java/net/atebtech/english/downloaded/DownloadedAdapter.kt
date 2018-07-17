package net.atebtech.english.downloaded

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_song.view.*
import net.atebtech.english.R
import net.atebtech.english.model.Song

class DownloadedAdapter(
        private val mValues: List<Song>,
        private val mListener: OnListInteractionListener?) : RecyclerView.Adapter<DownloadedAdapter.ViewHolder>() {

    interface OnListInteractionListener {
        fun onListInteraction(item: Song?)
    }

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Song
            mListener?.onListInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        //holder.mIdView.text = item.id
        //holder.mContentView.text = item.content

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val description: TextView = mView.song_description_view
        val date: TextView = mView.song_date_view

        override fun toString(): String {
            return super.toString() + " '" + description.text + "'"
        }
    }
}

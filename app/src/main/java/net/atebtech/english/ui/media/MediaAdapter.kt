package net.atebtech.english.ui.media

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_song.view.*
import net.atebtech.english.R
import net.atebtech.english.media.extensions.isPlaying
import net.atebtech.english.model.Song

class MediaAdapter(private val itemClickedListener: (Song) -> Unit,
                   private val playClickedListener: (Song) -> Unit) : RecyclerView.Adapter<MediaAdapter.ViewHolder>() {
    private val dataSet: MutableList<Song> = ArrayList()
    private val onClickListener: View.OnClickListener
    private val onPlayClickListener: View.OnClickListener
    private var currentSong: SongListViewModel.CurrentSong? = null

    init {
        onClickListener = View.OnClickListener { v ->
            //val item = v.tag as Song
            //itemClickedListener(item)
        }

        onPlayClickListener = View.OnClickListener { v ->
            val item = v.tag as Song
            playClickedListener(item)
        }
    }

    fun replaceData(dataSet: List<Song>) {
        setList(dataSet)
        notifyDataSetChanged()
    }

    private fun setList(dataSet: List<Song>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.description.text = item.title
        //holder.mContentView.text = item.content

        with(holder.play) {
            tag = item
            setOnClickListener(onPlayClickListener)
        }

        with(holder.view) {
            tag = item
            setOnClickListener(onClickListener)
        }

        holder.play.setImageResource(getResourceForMediaId(item))
    }

    private fun getResourceForMediaId(item: Song): Int {
        currentSong?.let {
            val isActive = item.id == it.id
            val isPlaying = it.playbackState?.isPlaying ?: false

            return when {
                !isActive -> R.drawable.ic_play_circle_outline_black_36dp
                isPlaying -> R.drawable.ic_pause_circle_outline_black_24dp
                else -> R.drawable.ic_play_circle_outline_black_36dp
            }
        }
        return R.drawable.ic_play_circle_outline_black_36dp
    }

    override fun getItemCount(): Int = dataSet.size

    fun updateData(it: SongListViewModel.CurrentSong?) {
        currentSong = it
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val description: TextView = view.song_description_view
        val date: TextView = view.song_date_view
        var play: ImageView = view.song_play_view

        override fun toString(): String {
            return super.toString() + " '" + description.text + "'"
        }
    }
}

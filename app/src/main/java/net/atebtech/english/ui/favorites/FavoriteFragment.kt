package net.atebtech.english.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_favorite.*
import net.atebtech.english.R
import net.atebtech.english.model.Song

class FavoriteFragment : Fragment(), FavoriteAdapter.OnListInteractionListener {
    private var listener: FavoriteAdapter.OnListInteractionListener? = this
//    lateinit var mediaSessionConnection: MediaSessionConnection

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val initialTopPosition = favorite_recycler_view.top
        with(favorite_recycler_view) {
            layoutManager = LinearLayoutManager(context)
            adapter = FavoriteAdapter(Song.ITEMS, listener)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        if (recyclerView.getChildAt(0).top < initialTopPosition) {
                            favorite_title_view.elevation = 8.0f
                        } else {
                            favorite_title_view.elevation = 0.0f
                        }
                    }
                }
            })
        }

//        mediaSessionConnection = MediaSessionConnection.getInstance(requireActivity().applicationContext,
//                ComponentName(context, MusicService::class.java))
//
//        Handler().postDelayed({
//            playMedia()
//        }, 1000)
    }


//    fun playMedia() {
//        val id = "1"
//        val nowPlaying = mediaSessionConnection.nowPlaying.value
//        val transportControls = mediaSessionConnection.transportControls
//
//        val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
//        if (isPrepared && id == nowPlaying?.id) {
//            mediaSessionConnection.playbackState.value?.let { playbackState ->
//                when {
//                    playbackState.isPlaying -> transportControls.pause()
//                    playbackState.isPlayEnabled -> transportControls.play()
//                    else -> {
//                        Log.w(TAG, "Playable item clicked but neither play nor pause are enabled!" +
//                                " (mediaId=${id})")
//                    }
//                }
//            }
//        } else {
//            transportControls.playFromMediaId("https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/01_-_Intro_-_The_Way_Of_Waking_Up_feat_Alan_Watts.mp3", null)
//        }
//    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onListInteraction(item: Song?) {

    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteFragment()
    }
}

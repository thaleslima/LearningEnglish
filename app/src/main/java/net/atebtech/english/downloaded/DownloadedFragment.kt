package net.atebtech.english.downloaded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_downloaded.*
import net.atebtech.english.R
import net.atebtech.english.model.Song

class DownloadedFragment : Fragment(), DownloadedAdapter.OnListInteractionListener {
    private var listener: DownloadedAdapter.OnListInteractionListener? = this

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_downloaded, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val initialTopPosition = downloaded_recycler_view.top
        with(downloaded_recycler_view) {
            layoutManager = LinearLayoutManager(context)
            adapter = DownloadedAdapter(Song.ITEMS, listener)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        if (recyclerView.getChildAt(0).top < initialTopPosition) {
                            downloaded_title_view.elevation = 8.0f
                        } else {
                            downloaded_title_view.elevation = 0.0f
                        }
                    }
                }
            })
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onListInteraction(item: Song?) {
    }

    companion object {
        @JvmStatic
        fun newInstance() = DownloadedFragment()
    }
}

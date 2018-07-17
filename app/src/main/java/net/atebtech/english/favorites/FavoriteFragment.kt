package net.atebtech.english.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_home.*
import net.atebtech.english.R
import net.atebtech.english.model.Song

class FavoriteFragment : Fragment(), FavoriteAdapter.OnListInteractionListener {
    private var listener: FavoriteAdapter.OnListInteractionListener? = this

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
    }

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
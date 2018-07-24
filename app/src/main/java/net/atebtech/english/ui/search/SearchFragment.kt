package net.atebtech.english.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search.*
import net.atebtech.english.R
import net.atebtech.english.model.Song

class SearchFragment : Fragment() {
    private var listener: SearchAdapter.OnListInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(search_recycler_view) {
            layoutManager = LinearLayoutManager(context)
            adapter = SearchAdapter(Song.ITEMS, listener)
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}

package net.atebtech.english.ui.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_category.*
import net.atebtech.english.R
import net.atebtech.english.utilities.InjectorUtils

class MediaFragment : Fragment() {
    private lateinit var viewModel: SongListViewModel

    private var adapter: MediaAdapter = MediaAdapter({

    }, {
        viewModel.mediaItemClicked(it)
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        initData()
    }

    private fun initData() {
        val factory = InjectorUtils.provideSongListViewModelFactory(requireContext())
        viewModel = ViewModelProviders.of(this, factory).get(SongListViewModel::class.java)

        viewModel.getSongs().observe(viewLifecycleOwner, Observer { plants ->
            if (plants != null) adapter.replaceData(plants)
        })

        viewModel.getCurrentSong().observe(viewLifecycleOwner, Observer { plants ->
            if (plants != null) adapter.updateData(plants)
        })
    }

    private fun initRecycler() {
        val initialTopPosition = category_recycler_view.top
        with(category_recycler_view) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@MediaFragment.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        if (recyclerView.getChildAt(0).top < initialTopPosition) {
                            category_title_view.elevation = 8.0f
                        } else {
                            category_title_view.elevation = 0.0f
                        }
                    }
                }
            })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MediaFragment()
    }
}

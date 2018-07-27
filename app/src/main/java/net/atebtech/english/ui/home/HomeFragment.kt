package net.atebtech.english.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import net.atebtech.english.R
import net.atebtech.english.ui.media.MediaFragment
import net.atebtech.english.model.Category
import net.atebtech.english.ui.main.MainActivity

class HomeFragment : Fragment() {

    private val adapterList = HomeAdapter {
        val m = requireActivity() as MainActivity
        m.switchContent(MediaFragment.newInstance())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        val initialTopPosition = home_recycler_view.top
        with(home_recycler_view) {
            layoutManager = GridLayoutManager(context, COLUMN_COUNT)

            addItemDecoration(net.atebtech.english.ui.home.GridSpacingItemDecoration(COLUMN_COUNT, 32, true))
            adapter = adapterList

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        if (recyclerView.getChildAt(0).top < initialTopPosition) {
                            home_title_view.elevation = 8.0f
                        } else {
                            home_title_view.elevation = 0.0f
                        }
                    }
                }
            })
        }

        adapterList.replaceData(Category.ITEMS)
    }

    companion object {
        const val COLUMN_COUNT = 2

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}

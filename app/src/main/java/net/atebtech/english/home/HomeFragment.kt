package net.atebtech.english.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import net.atebtech.english.MainActivity
import net.atebtech.english.R
import net.atebtech.english.category.CategoryFragment
import net.atebtech.english.model.Category

class HomeFragment : Fragment(), HomeAdapter.OnListInteractionListener {
    private var listener: HomeAdapter.OnListInteractionListener? = this

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

            addItemDecoration(net.atebtech.english.home.GridSpacingItemDecoration(COLUMN_COUNT, 32, true))
            adapter = net.atebtech.english.home.HomeAdapter(Category.ITEMS, listener)

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
    }

    override fun onListInteraction(item: Category?) {
        val m = requireActivity() as MainActivity
        m.switchContent(CategoryFragment.newInstance())
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        const val COLUMN_COUNT = 2

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}

package net.atebtech.english.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_player.*
import net.atebtech.english.R
import net.atebtech.english.ui.downloaded.DownloadedFragment
import net.atebtech.english.ui.favorites.FavoriteFragment
import net.atebtech.english.ui.home.HomeFragment
import net.atebtech.english.ui.search.SearchFragment
import net.atebtech.english.utilities.InjectorUtils

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                switchContent(HomeFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                switchContent(SearchFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                switchContent(FavoriteFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_downloaded -> {
                switchContent(DownloadedFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun switchContent(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.main_content, fragment)
            commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            switchContent(HomeFragment.newInstance())
        }

        play_view.setOnClickListener {
//            PlayerActivity.navigate(it.context)

            val id = it.tag as String
            viewModel.mediaItemClicked(id)
        }

        initData()
    }

    private fun initData() {
        val factory = InjectorUtils.provideMainViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)

        viewModel.getCurrentSong().observe(this, Observer { plants ->
            if (plants != null && plants.show) {
                play_description_view.text = plants.title
                play_description_aux_view.text = plants.album
                play_view.setImageResource(getResourceForMediaId(plants.play))
                play_view.tag = plants.id
                player.visibility = View.VISIBLE
            }
        })
    }

    private fun getResourceForMediaId(play: Boolean): Int {
        return when {
            !play -> R.drawable.ic_play_arrow_white_24dp
            else -> R.drawable.ic_pause_white_24dp
        }
    }
}

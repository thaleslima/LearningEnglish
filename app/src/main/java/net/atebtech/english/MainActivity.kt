package net.atebtech.english

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import net.atebtech.english.downloaded.DownloadedFragment
import net.atebtech.english.favorites.FavoriteFragment
import net.atebtech.english.home.HomeFragment
import net.atebtech.english.search.SearchFragment

class MainActivity : AppCompatActivity() {

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

        findViewById<View>(R.id.play_view).setOnClickListener {
            PlayerActivity.navigate(it.context)
        }
    }
}

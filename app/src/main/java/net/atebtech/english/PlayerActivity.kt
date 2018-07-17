package net.atebtech.english

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import android.text.method.ScrollingMovementMethod

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        findViewById<TextView>(R.id.play_content_view).text = Html.fromHtml(getString(R.string.cat_bottom_nav_page_2_name))
        findViewById<TextView>(R.id.play_content_view).movementMethod = ScrollingMovementMethod()
    }

    companion object {
        fun navigate(context: Context) {
            val intent = Intent(context, PlayerActivity::class.java)
            context.startActivity(intent)
        }
    }
}

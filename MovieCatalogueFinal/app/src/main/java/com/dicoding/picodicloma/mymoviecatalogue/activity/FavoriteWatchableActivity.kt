package com.dicoding.picodicloma.mymoviecatalogue.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import com.dicoding.picodicloma.mymoviecatalogue.R
import com.dicoding.picodicloma.mymoviecatalogue.adapter.WatchableFragmentPagerAdapter
import com.dicoding.picodicloma.mymoviecatalogue.viewmodel.WatchableViewModel
import kotlinx.android.synthetic.main.activity_main.*

class FavoriteWatchableActivity : AppCompatActivity() {

    private lateinit var watchableViewModel: WatchableViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        invalidateOptionsMenu()

        val fragmentPagerAdapter =
            WatchableFragmentPagerAdapter.newInstance(
                this,
                supportFragmentManager,
                isFavoriteActivity = true
            )

        view_pager.adapter = fragmentPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.setTitle(getString(R.string.title_bar_favorite))
        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        menu?.findItem(R.id.action_favorite)?.apply{
            isVisible = false
        }
        menu?.findItem(R.id.action_search)?.apply{
            isVisible = false
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.action_home -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}

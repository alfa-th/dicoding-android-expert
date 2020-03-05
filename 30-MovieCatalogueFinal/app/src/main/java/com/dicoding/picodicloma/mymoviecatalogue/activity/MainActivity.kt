package com.dicoding.picodicloma.mymoviecatalogue.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.picodicloma.mymoviecatalogue.R
import com.dicoding.picodicloma.mymoviecatalogue.adapter.WatchableFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName

        val EXTRA_QUERY = "extra_query"
        val EXTRA_TYPE = "extra_type"
    }

    private lateinit var fragmentPagerAdapter: FragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentPagerAdapter =
            WatchableFragmentPagerAdapter.newInstance(
                this,
                supportFragmentManager,
                isFavoriteActivity = false
            )

        view_pager.adapter = fragmentPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.title = getString(R.string.title_bar_normal)
        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val homeMenuItem = menu?.findItem(R.id.action_home)
        homeMenuItem?.isVisible = false

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.action_search)?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isSubmitButtonEnabled = true
            queryHint = "AAAA"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    val queryIntent =
                        Intent(this@MainActivity, SearchableActivity::class.java).apply {
                            putExtra(EXTRA_QUERY, query)
                            putExtra(EXTRA_TYPE, tabs.selectedTabPosition)
                        }

                    startActivity(queryIntent)

                    return true
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.action_favorite -> {
                val mIntent = Intent(this@MainActivity, FavoriteWatchableActivity::class.java)
                startActivity(mIntent)
            }
            R.id.action_notification -> {
                val mIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(mIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}

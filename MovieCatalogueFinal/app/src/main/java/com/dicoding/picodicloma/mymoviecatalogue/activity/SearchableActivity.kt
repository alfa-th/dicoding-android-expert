package com.dicoding.picodicloma.mymoviecatalogue.activity

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodicloma.mymoviecatalogue.R
import com.dicoding.picodicloma.mymoviecatalogue.adapter.WatchableAdapter
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable
import com.dicoding.picodicloma.mymoviecatalogue.viewmodel.FavoriteWatchableViewModel
import com.dicoding.picodicloma.mymoviecatalogue.viewmodel.SearchableViewModel
import kotlinx.android.synthetic.main.activity_searchable.*

class SearchableActivity : AppCompatActivity() {
    companion object {
        const val TYPE_MOVIE = 0
        const val TYPE_TV = 1

        const val ARG_QUERY_TYPE = "query_type"
        const val ARG_QUERY_CURRENT = "query_current"

        val TAG = SearchableActivity::class.java.simpleName
        const val watchableActivityForResultRequestCode = 1
    }

    private lateinit var watchableAdapter: WatchableAdapter
    private lateinit var searchableViewModel: SearchableViewModel
    private lateinit var favoriteWatchableViewModel: FavoriteWatchableViewModel
    private lateinit var searchView: SearchView
    private var currentQuery: String? = null
    var queryType = TYPE_MOVIE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)
        supportActionBar?.title = ""

        savedInstanceState?.let {
            queryType = it.getInt(ARG_QUERY_TYPE, TYPE_MOVIE)
        }

        if (currentQuery == null) {
            intent.getStringExtra(MainActivity.EXTRA_QUERY)?.let { queryFromIntent ->
                currentQuery = queryFromIntent
            }
        } else {
            savedInstanceState?.let {
                currentQuery = it.getString(ARG_QUERY_CURRENT, "")
            }
        }

        queryType =
            if (intent?.getIntExtra(MainActivity.EXTRA_TYPE, 0) == 0) TYPE_MOVIE else TYPE_TV

        showRecyclerList()

        rv_watchables.setHasFixedSize(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(ARG_QUERY_TYPE, queryType)

        searchableViewModel.currentQuery.observe(this, Observer { queryFromViewModel ->
            outState.putString(ARG_QUERY_CURRENT, queryFromViewModel)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == watchableActivityForResultRequestCode && resultCode == Activity.RESULT_OK) {
            data?.let { intent ->
                var isFavoriteChanged =
                    intent.getBooleanExtra(WatchableActivity.EXTRA_CHANGED_REPLY, false)
                var newState =
                    intent.getBooleanExtra(WatchableActivity.EXTRA_TYPE_REPLY, false)

                Log.d(TAG, "isFavoriteChanged : $isFavoriteChanged")
                Log.d(TAG, "newState : $newState")

                if (isFavoriteChanged) {
                    intent.getParcelableExtra<Watchable>(WatchableActivity.EXTRA_WATCHABLE_REPLY)
                        ?.let { watchableData ->
                            if (newState) {
                                favoriteWatchableViewModel.insert(watchableData)
                            } else {
                                favoriteWatchableViewModel.delete(watchableData)
                            }
                        }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_searchable, menu)
        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isSubmitButtonEnabled = true
            queryHint =
                if (queryType == TYPE_MOVIE) getString(R.string.search_film) else getString(R.string.search_tv)
            isIconifiedByDefault = false
            maxWidth = Integer.MAX_VALUE
            setQuery(currentQuery, false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { queryFromSubmit ->
                        searchableViewModel.let {
                            it.queryByTitle(this@SearchableActivity, queryFromSubmit)
                        }

                        showLoading(true)
                        Log.d(TAG, "queryFromSubmit on onQueryTextSubmit : $queryFromSubmit")
                    }
                    return true
                }
            })
        }

        Log.d(TAG, "currentQuery on onCreateOptions $currentQuery")
        return super.onCreateOptionsMenu(menu)
    }

    private fun showRecyclerList() {
        rv_watchables.layoutManager = LinearLayoutManager(this)
        watchableAdapter =
            WatchableAdapter()
        rv_watchables.adapter = watchableAdapter

        searchableViewModel = ViewModelProvider(
            this
        )
            .get(SearchableViewModel::class.java)

        favoriteWatchableViewModel = ViewModelProvider(
            this
        )
            .get(FavoriteWatchableViewModel::class.java)

        searchableViewModel.viewModelType = queryType
        searchableViewModel.let {
            currentQuery?.let { query ->
                it.queryByTitle(this, query)
            }
            it.listSearchWatchable.observe(
                this,
                Observer { watchableItems ->
                    watchableItems.let { movieItems ->
                        watchableAdapter.setData(movieItems as ArrayList<Watchable>)
                        showLoading(false)
                    }

                    it.currentQuery.observe(
                        this,
                        Observer { currentQuery ->
                            if (watchableAdapter.itemCount == 0)
                                toastShowText("0 result for query \"$currentQuery\"")
                        }
                    )
                })
        }

        watchableAdapter.setOnItemClickCallback(
            object :
                WatchableAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Watchable) {
                    toastShowText("${getString(R.string.you_have_chosen)} ${data.title}")
//                var isFavorite: Boolean? = false

                    data.title.let { title ->
                        favoriteWatchableViewModel
                            .isFavoriteByTitle(title)
                            .observeOnce(
                                this@SearchableActivity,
                                Observer {
                                    if (it != null) {
                                        val moveWithObjectIntentForResult =
                                            Intent(
                                                this@SearchableActivity,
                                                WatchableActivity::class.java
                                            ).apply {
                                                putExtra(
                                                    WatchableActivity.EXTRA_WATCHABLE,
                                                    data
                                                )
                                                putExtra(
                                                    WatchableActivity.EXTRA_TYPE,
                                                    it
                                                )
                                            }

                                        startActivityForResult(
                                            moveWithObjectIntentForResult,
                                            watchableActivityForResultRequestCode
                                        )
                                        Log.d(TAG, "isFavorite : $it")
                                        Log.d(TAG, "Intent: $moveWithObjectIntentForResult")
                                    }
                                }
                            )
                    }
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected: ${item.title}")

        return super.onOptionsItemSelected(item)
    }


    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }


    private fun showLoading(state: Boolean) {
        if (state)
            progress_circular.visibility = View.VISIBLE
        else
            progress_circular.visibility = View.GONE
    }

    private fun toastShowText(str: String) {
        Toast.makeText(
            this,
            str,
            Toast.LENGTH_SHORT
        )
            .show()
    }
}

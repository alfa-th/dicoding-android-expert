package com.dicoding.picodicloma.mymoviecatalogue.fragment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodicloma.mymoviecatalogue.R
import com.dicoding.picodicloma.mymoviecatalogue.activity.WatchableActivity
import com.dicoding.picodicloma.mymoviecatalogue.adapter.WatchableAdapter
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable
import com.dicoding.picodicloma.mymoviecatalogue.viewmodel.FavoriteWatchableViewModel
import com.dicoding.picodicloma.mymoviecatalogue.viewmodel.WatchableViewModel
import com.dicoding.picodicloma.mymoviecatalogue.widget.FavoriteWatchableBannerWidget
import kotlinx.android.synthetic.main.fragment_watchable.*


class WatchableFragment : Fragment() {

    companion object {
        const val TYPE_MOVIE = 0
        const val TYPE_TV = 1

        const val watchableActivityForResultRequestCode = 1

        private var ARG_FRAGMENT_TYPE = "fragment_type"
        private var ARG_IS_FAVORITE = "is_favorite"

        private val TAG = WatchableFragment::class.java.simpleName

        fun newInstance(fragmentType: Int, isFavoriteFragment: Boolean): WatchableFragment {
            val fragment = WatchableFragment()
            fragment.fragmentType = fragmentType
            fragment.isFavoriteFragment = isFavoriteFragment

            val bundle = Bundle()
            bundle.putInt(ARG_FRAGMENT_TYPE, fragmentType)
            bundle.putBoolean(ARG_IS_FAVORITE, isFavoriteFragment)
            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var watchableAdapter: WatchableAdapter
    private lateinit var watchableViewModel: WatchableViewModel
    private lateinit var favoriteWatchableViewModel: FavoriteWatchableViewModel
    private var fragmentType: Int = 0
    private var isFavoriteFragment: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watchable, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)

        if (savedInstanceState != null) {
            fragmentType = savedInstanceState.getInt(ARG_FRAGMENT_TYPE, 0)
            isFavoriteFragment = savedInstanceState.getBoolean(ARG_IS_FAVORITE, false)
        }

        showRecyclerList()
        rv_watchables.setHasFixedSize(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(ARG_FRAGMENT_TYPE, fragmentType)
        outState.putBoolean(ARG_IS_FAVORITE, isFavoriteFragment)
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

                            context?.run {
                                FavoriteWatchableBannerWidget.notifyWidgetDataChanged(this)
                            }
                        }
                }
            }
        }
    }

    private fun showRecyclerList() {
        rv_watchables.layoutManager = LinearLayoutManager(this.context)
        watchableAdapter =
            WatchableAdapter()
        rv_watchables.adapter = watchableAdapter

        favoriteWatchableViewModel = ViewModelProvider(
            this
        )
            .get(FavoriteWatchableViewModel::class.java)

        if (isFavoriteFragment) {
            if (fragmentType == TYPE_MOVIE) {
                favoriteWatchableViewModel.allFavoriteWatchables.observe(
                    viewLifecycleOwner,
                    Observer { watchableItems ->
                        watchableItems?.filter {
                            it.isMovie == true
                        }?.let { movieItems ->
                            watchableAdapter.setData(movieItems as ArrayList<Watchable>)
                            showLoading(false)
                        }
                    })
            } else {
                favoriteWatchableViewModel.allFavoriteWatchables.observe(
                    viewLifecycleOwner,
                    Observer { watchableItems ->
                        watchableItems?.filter {
                            it.isMovie == false
                        }?.let { movieItems ->
                            watchableAdapter.setData(movieItems as ArrayList<Watchable>)
                            showLoading(false)
                        }
                    })
            }
        } else {
            watchableViewModel = ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            )
                .get(WatchableViewModel::class.java)

            watchableViewModel.setType(fragmentType)
            watchableViewModel.setWatchable(this.context)
            watchableViewModel
                .getWatchable()
                .observe(
                    viewLifecycleOwner,
                    Observer { watchableItems ->
                        watchableItems?.let {
                            watchableAdapter.setData(watchableItems)
                            showLoading(false)
                        }
                    }
                )
        }

        fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
            observe(lifecycleOwner, object : Observer<T> {
                override fun onChanged(t: T?) {
                    observer.onChanged(t)
                    removeObserver(this)
                }
            })
        }

        watchableAdapter.setOnItemClickCallback(object :
            WatchableAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Watchable) {
                toastShowText("${getString(R.string.you_have_chosen)} ${data.title}")
//                var isFavorite: Boolean? = false

                data.title.let { title ->
                    favoriteWatchableViewModel
                        .isFavoriteByTitle(title)
                        .observeOnce(
                            viewLifecycleOwner,
                            Observer {
                                if (it != null) {
                                    val moveWithObjectIntentForResult =
                                        Intent(
                                            this@WatchableFragment.context,
                                            WatchableActivity::class.java
                                        )

                                    moveWithObjectIntentForResult.putExtra(
                                        WatchableActivity.EXTRA_WATCHABLE,
                                        data
                                    )

                                    moveWithObjectIntentForResult.putExtra(
                                        WatchableActivity.EXTRA_TYPE,
                                        it
                                    )

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

    private fun showLoading(state: Boolean) {
        if (state)
            progress_circular.visibility = View.VISIBLE
        else
            progress_circular.visibility = View.GONE
    }

    private fun toastShowText(str: String) {
        Toast.makeText(
            this.context,
            str,
            Toast.LENGTH_SHORT
        )
            .show()
    }
}


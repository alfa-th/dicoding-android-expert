package com.dicoding.picodicloma.mymoviecatalogue.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodicloma.mymoviecatalogue.R
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable
import com.dicoding.picodicloma.mymoviecatalogue.model.WatchableEnum
import com.dicoding.picodicloma.mymoviecatalogue.viewmodel.WatchableViewModel
import com.dicoding.picodicloma.mymoviecatalogue.activity.WatchableActivity
import com.dicoding.picodicloma.mymoviecatalogue.adapter.WatchableAdapter
import kotlinx.android.synthetic.main.fragment_watchable.*

/**
 * A simple [Fragment] subclass.
 */
class WatchableFragment : Fragment() {

    companion object {
        private var EXTRA_FRAGMENT_TYPE = "fragment_type"
    }

    private lateinit var watchableAdapter: WatchableAdapter
    private lateinit var watchableViewModel: WatchableViewModel
    private lateinit var fragmentType: WatchableEnum

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

        if (savedInstanceState != null)
            fragmentType = savedInstanceState.getSerializable(EXTRA_FRAGMENT_TYPE) as WatchableEnum

        showRecyclerList()
        rv_watchables.setHasFixedSize(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable(EXTRA_FRAGMENT_TYPE, fragmentType)
    }

    internal fun setType(watchableType: WatchableEnum) {
        fragmentType = watchableType
    }

    private fun showRecyclerList() {
        rv_watchables.layoutManager = LinearLayoutManager(this.context)
        watchableAdapter =
            WatchableAdapter()
        rv_watchables.adapter = watchableAdapter

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
                this,
                Observer { watchableItems ->
                    if (watchableItems != null) {
                        watchableAdapter.setData(watchableItems)
                        showLoading(false)
                    }
                }
            )

        watchableAdapter.setOnItemClickCallback(object :
            WatchableAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Watchable) {
                Toast.makeText(
                    this@WatchableFragment.context,
                    "${getString(R.string.you_have_chosen)} ${data.title}",
                    Toast.LENGTH_SHORT
                ).show()
                val moveWithObjectIntent =
                    Intent(this@WatchableFragment.context, WatchableActivity::class.java)
                moveWithObjectIntent.putExtra(WatchableActivity.EXTRA_WATCHABLE, data)

                startActivity(moveWithObjectIntent)
            }
        })
    }

    internal fun showLoading(state: Boolean) {
        if (state)
            progress_circular.visibility = View.VISIBLE
        else
            progress_circular.visibility = View.GONE
    }
}

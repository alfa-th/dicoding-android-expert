package com.dicoding.picodicloma.moviecatalogue


import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_watchable.*

/**
 * A simple [Fragment] subclass.
 */
class WatchableFragment() : Fragment() {

    private var listWatchable = arrayListOf<Watchable>()
    private var watchableType = WatchableEnum.MOVIE

    companion object {
        private var STATE_LIST = "state_list"
        private var ARG_FRAGMENT_TYPE = "fragment_type"

        fun newInstance(watchableEnum: WatchableEnum) : WatchableFragment {
            val fragment = WatchableFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARG_FRAGMENT_TYPE, watchableEnum)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watchable, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_watchables.setHasFixedSize(true)

        if(getArguments() != null) {
            watchableType = arguments?.getSerializable(ARG_FRAGMENT_TYPE) as WatchableEnum
        }

        when {
            savedInstanceState == null -> {
                listWatchable.addAll(getListWatchables())
            }

            else -> {
                val stateList = savedInstanceState.getParcelableArrayList<Watchable>(STATE_LIST)

                if (stateList != null) {
                    listWatchable.addAll(getListWatchables())
                }
            }
        }

        showRecyclerList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList(STATE_LIST, listWatchable)
    }

    private fun getListWatchables(): ArrayList<Watchable> {

        lateinit var dataPhoto: TypedArray
        lateinit var dataTitle: Array<String>
        lateinit var dataYear: Array<String>
        lateinit var dataDirector: Array<String>
        lateinit var dataDescription: Array<String>

        val listWatchable = ArrayList<Watchable>()

        when (watchableType) {
            WatchableEnum.MOVIE -> {
                dataPhoto = resources.obtainTypedArray(R.array.movie_photo)
                dataTitle = resources.getStringArray(R.array.movie_title)
                dataYear = resources.getStringArray(R.array.movie_year)
                dataDirector = resources.getStringArray(R.array.movie_director)
                dataDescription = resources.getStringArray(R.array.movie_description)
            }


            WatchableEnum.TV -> {
                dataPhoto = resources.obtainTypedArray(R.array.tv_photo)
                dataTitle = resources.getStringArray(R.array.tv_title)
                dataYear = resources.getStringArray(R.array.tv_year)
                dataDirector = resources.getStringArray(R.array.tv_director)
                dataDescription = resources.getStringArray(R.array.tv_description)
            }
        }

        for (position in dataTitle.indices) {
            val watchable = Watchable(
                dataPhoto.getResourceId(position, -1),
                dataTitle[position],
                dataYear[position],
                dataDirector[position],
                dataDescription[position]
            )

            listWatchable.add(watchable)
        }

        dataPhoto.recycle()

        return listWatchable
    }

    private fun showRecyclerList() {
        rv_watchables.layoutManager = LinearLayoutManager(this.context)
        val listHeroAdapter = ListWatchableAdapter(listWatchable)
        rv_watchables.adapter = listHeroAdapter

        listHeroAdapter.setOnItemClickCallback(object :
            ListWatchableAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Watchable) {
                Toast.makeText(
                    this@WatchableFragment.context,
                    "Kamu memilih ${data.title}",
                    Toast.LENGTH_SHORT
                ).show()

                val moveWithObjectIntent = Intent(this@WatchableFragment.context, WatchableActivity::class.java)
                moveWithObjectIntent.putExtra(WatchableActivity.EXTRA_WATCHABLE, data)

                startActivity(moveWithObjectIntent)
            }
        })
    }

}

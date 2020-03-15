package com.dicoding.picodicloma.mymoviecatalogue.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.picodicloma.mymoviecatalogue.R
import com.dicoding.picodicloma.mymoviecatalogue.fragment.WatchableFragment

class WatchableFragmentPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        fun newInstance(
            context: Context,
            fm: FragmentManager,
            isFavoriteActivity: Boolean
        ): WatchableFragmentPagerAdapter {
            val newWatchableFragmentPagerAdapter = WatchableFragmentPagerAdapter(context, fm)
            newWatchableFragmentPagerAdapter.isFavoriteActivity = isFavoriteActivity

            return newWatchableFragmentPagerAdapter
        }
    }

    private var isFavoriteActivity: Boolean = false

    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
    )



    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> {
                fragment =
                    WatchableFragment.newInstance(WatchableFragment.TYPE_MOVIE, isFavoriteActivity)
            }

            1 -> {
                fragment =
                    WatchableFragment.newInstance(WatchableFragment.TYPE_TV, isFavoriteActivity)
            }
        }

        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}
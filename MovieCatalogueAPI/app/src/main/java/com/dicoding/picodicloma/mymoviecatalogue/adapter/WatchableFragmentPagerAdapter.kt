package com.dicoding.picodicloma.mymoviecatalogue.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.picodicloma.mymoviecatalogue.R
import com.dicoding.picodicloma.mymoviecatalogue.model.WatchableEnum
import com.dicoding.picodicloma.mymoviecatalogue.fragment.WatchableFragment

class WatchableFragmentPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

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
                    WatchableFragment()
                fragment.setType(WatchableEnum.MOVIE)
            }

            1 -> {
                fragment =
                    WatchableFragment()
                fragment.setType(WatchableEnum.TV_SHOWS)
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
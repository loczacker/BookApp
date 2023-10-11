package com.zacker.bookapp.ui.home


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zacker.bookapp.ui.homeBookCase.HomeBookCaseFragment
import com.zacker.bookapp.ui.homeDiscover.HomeDiscoverFragment
import com.zacker.bookapp.ui.homeProfile.HomeProfileFragment

class ViewPagerHomeAdapter(fragment: HomeFragment):  FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeBookCaseFragment()
            1 -> HomeDiscoverFragment()
            2 -> HomeProfileFragment()
            else -> throw  IllegalArgumentException("Unknown Fragment")
        }
    }
}
package com.zacker.bookapp.ui.homeAdmin


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zacker.bookapp.ui.home.HomeFragment
import com.zacker.bookapp.ui.homeAdmin.createBook.CreateBookFragment
import com.zacker.bookapp.ui.homeBookCase.HomeBookCaseFragment
import com.zacker.bookapp.ui.homeDiscover.HomeDiscoverFragment
import com.zacker.bookapp.ui.homeProfile.HomeProfileFragment

class ViewPagerHomeAdminAdapter(fragment: HomeAdminFragment):  FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            1 -> CreateBookFragment()
            0 -> HomeDiscoverFragment()
            2 -> HomeProfileFragment()
            else -> throw  IllegalArgumentException("Unknown Fragment")
        }
    }
}
package com.zacker.bookapp.ui.homeBookCase

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zacker.bookapp.ui.homeBookCase.booksViewed.BooksViewedFragment
import com.zacker.bookapp.ui.homeBookCase.favourite.FavouriteFragment

class DataPagerBookCaseAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int  = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> BooksViewedFragment()
            1 -> FavouriteFragment()
            else -> throw  IllegalArgumentException("Unknown Fragment")
        }
    }
}
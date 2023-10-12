package com.zacker.bookapp.ui.homeAdmin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.FragmentHomeAdminBinding
import com.zacker.bookapp.ui.home.ViewPagerHomeAdapter


class HomeAdminFragment : Fragment() {

    private lateinit var binding: FragmentHomeAdminBinding
    private var isViewPagerScrollEnabled = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()
        setUpBottom()
    }

    private fun setUpBottom() {
            val viewPagerHomeAdminAdapter = ViewPagerHomeAdminAdapter(this)
        binding.vpShare.adapter = viewPagerHomeAdminAdapter
        binding.vpShare.isUserInputEnabled = isViewPagerScrollEnabled
        binding.vpShare.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    1 -> binding.bottomNavigationView.menu.findItem(R.id.nav_book_create).isChecked = true
                    0 -> binding.bottomNavigationView.menu.findItem(R.id.nav_discover).isChecked = true
                    2 -> binding.bottomNavigationView.menu.findItem(R.id.nav_profile).isChecked = true
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                isViewPagerScrollEnabled = state != ViewPager2.SCROLL_STATE_DRAGGING
                binding.vpShare.isUserInputEnabled = isViewPagerScrollEnabled
            }
        })
    }

    private fun setUpViewPager() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_book_case -> binding.vpShare.currentItem = 1
                R.id.nav_discover -> binding.vpShare.currentItem = 0
                R.id.nav_profile -> binding.vpShare.currentItem = 2
            }
            true
        })
    }
}
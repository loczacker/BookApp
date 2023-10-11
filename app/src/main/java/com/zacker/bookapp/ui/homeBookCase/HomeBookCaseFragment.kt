package com.zacker.bookapp.ui.homeBookCase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.zacker.bookapp.databinding.FragmentHomeBookCaseBinding

class HomeBookCaseFragment : Fragment() {

    private lateinit var binding: FragmentHomeBookCaseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBookCaseBinding.inflate(inflater, container, false)
        binding.vpShare.adapter = DataPagerBookCaseAdapter(requireActivity() as AppCompatActivity)
        val tabTitles = arrayOf("Đã xem", "Yêu thích")
        TabLayoutMediator(binding.tabBookCase, binding.vpShare) {
                tab, position -> tab.text = tabTitles[position]
        }.attach()
        return binding.root
    }
}
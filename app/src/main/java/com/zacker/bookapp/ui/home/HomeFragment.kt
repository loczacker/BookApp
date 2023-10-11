package com.zacker.bookapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Date

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var database: FirebaseDatabase
    private var isViewPagerScrollEnabled = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance()
        addDatabase()
        setUpViewPager()
        setUpBottom()
    }

    private fun addDatabase() {
//        val timeStamp = System.currentTimeMillis()
//        val hashMap: HashMap<String, Any> = HashMap()
//        val nameBook = "Người Người Cưng Chiều Sư Muội Miệng Quạ Đen"
//        hashMap["writerName"] = "Hùng Ngận Manh"
//        hashMap["img"] = "https://firebasestorage.googleapis.com/v0/b/bookapp-3329e.appspot.com/o/img%2Fnguoinguoicungchieu.jpeg?alt=media&token=054368e1-64f5-42ee-805b-1a9abe67eabb"
//        hashMap["introduction"] = "Hoa Linh Cơ chưa bao giờ ngờ tới, kiếp trước mình là sữa độc (*), kiếp này lại thành quạ đen, thiên phú nguyền rủa level max. Có còn để nàng sống nữa không hả!"
//        hashMap["category"] = "Tiên Hiệp"
//        hashMap["chapter"] = ""
//        hashMap["timeStamp"] = timeStamp
//        database.reference.child("Books").child(nameBook).setValue(hashMap).addOnCompleteListener{
//            if (it.isSuccessful){
//            }
//        }
    }

    private fun setUpBottom() {
        val viewPagerHomeAdapter = ViewPagerHomeAdapter(this)
        binding.vpShare.adapter = viewPagerHomeAdapter
        binding.vpShare.isUserInputEnabled = isViewPagerScrollEnabled  // Bật hoặc tắt sự kiện vuốt
        binding.vpShare.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.bottomNavigationView.menu.findItem(R.id.nav_book_case).isChecked = true
                    1 -> binding.bottomNavigationView.menu.findItem(R.id.nav_discover).isChecked = true
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
                R.id.nav_book_case -> binding.vpShare.currentItem = 0
                R.id.nav_discover -> binding.vpShare.currentItem = 1
                R.id.nav_profile -> binding.vpShare.currentItem = 2
            }
            true
        })
    }


}

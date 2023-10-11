package com.zacker.bookapp.ui.homeDiscover

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.FragmentHomeDiscoverBinding
import com.zacker.bookapp.model.BooksModel
import com.zacker.bookapp.ui.allbook.AllBookAdapter
import java.util.ArrayList

class HomeDiscoverFragment : Fragment(), NewBookAdapter.OnBookItemClickListener,
RandomBookAdapter.OnBookItemClickListener{
    private lateinit var binding: FragmentHomeDiscoverBinding
    private lateinit var viewModel: HomeDiscoverViewModel

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val listNewBook: ArrayList<BooksModel> = arrayListOf()
    private val listRandomBook: ArrayList<BooksModel> = arrayListOf()

    private lateinit var randomBookAdapter: RandomBookAdapter
    private lateinit var newBookAdapter: NewBookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeDiscoverBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeDiscoverViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        getNewBook()
        getRandomBook()
    }

    private fun getRandomBook() {
        randomBookAdapter = RandomBookAdapter(listRandomBook, this)
        binding.rvRandomBook.adapter = randomBookAdapter
        database.reference.child("Books")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val randomBooks = mutableListOf<BooksModel>()

                    for (bookSnapshot in snapshot.children) {
                        val nameBook = bookSnapshot.child("nameBook").getValue(String::class.java)
                        val img = bookSnapshot.child("img").getValue(String::class.java)
                        val writerName = bookSnapshot.child("writerName").getValue(String::class.java)
                        val introduction = bookSnapshot.child("introduction").getValue(String::class.java)
                        val category = bookSnapshot.child("category").getValue(String::class.java)

                        if (nameBook != null && img != null && writerName != null && introduction != null) {
                            val book = BooksModel(nameBook, writerName, img, introduction, category)
                            randomBooks.add(book)
                        }
                    }

                    // Sắp xếp danh sách sách ngẫu nhiên
                    randomBooks.shuffle()

                    // Giới hạn số sách bạn muốn hiển thị (ví dụ: 5 sách ngẫu nhiên)
                    val randomBooksToShow = randomBooks.take(10)
                    listRandomBook.clear()
                    listRandomBook.addAll(randomBooksToShow)
                    randomBookAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Database Error: $error")
                }
            })
    }

    private fun getNewBook() {
        newBookAdapter = NewBookAdapter(listNewBook, this)
        binding.rvNewBook.adapter = newBookAdapter

        database.reference.child("Books")
            .orderByChild("timeStamp")
            .limitToLast(10)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listNewBook.clear()
                    for (bookSnapshot in snapshot.children) {
                        val nameBook = bookSnapshot.child("nameBook").getValue(String::class.java)
                        val img = bookSnapshot.child("img").getValue(String::class.java)
                        val writerName = bookSnapshot.child("writerName").getValue(String::class.java)
                        val introduction = bookSnapshot.child("introduction").getValue(String::class.java)
                        val category = bookSnapshot.child("category").getValue(String::class.java)
                        if (nameBook != null && img != null && writerName != null) {
                            val book = BooksModel(nameBook, writerName, img, introduction, category)
                            listNewBook.add(book)
                        }
                    }
                    listNewBook.sortByDescending { it.timeStamp }
                    newBookAdapter.notifyItemInserted(listNewBook.size)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Database Error: $error")
                }
            })
    }


    private fun setListeners() {
        binding.ibNewBook.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.home_to_all_book, null)
        }
        binding.ibRandomBook.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.home_to_all_book, null)
        }
    }

    override fun onClick(position: Int) {
        val selectedBook = listNewBook[position]
        val bundle = Bundle()
        bundle.putSerializable("selectedBook", selectedBook)
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_bookIntroductionFragment, bundle)
    }

    override fun onClickBook(position: Int) {
        val selectedBook = listRandomBook[position]
        val bundle = Bundle()
        bundle.putSerializable("selectedBook", selectedBook)
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_bookIntroductionFragment, bundle)
    }
}
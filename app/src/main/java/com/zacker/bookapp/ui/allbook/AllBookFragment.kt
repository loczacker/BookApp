package com.zacker.bookapp.ui.allbook

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.FragmentAllBookBinding
import com.zacker.bookapp.model.BooksModel
import com.zacker.bookapp.ui.allbook.AllBookAdapter
import java.util.ArrayList

class AllBookFragment : Fragment(), AllBookAdapter.OnBookItemClickListener {

    private lateinit var binding: FragmentAllBookBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val listBook: ArrayList<BooksModel> = arrayListOf()

    private lateinit var allBookAdapter: AllBookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setListeners()
        getAllBook()
    }

    private fun setupRecyclerView() {
        allBookAdapter = AllBookAdapter(listBook, this)
        binding.recyclerViewAllBook.adapter = allBookAdapter
    }

    private fun getAllBook() {
        database.reference.child("Books")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listBook.clear()
                    for (bookSnapshot in snapshot.children) {
                        val nameBook = bookSnapshot.child("nameBook").getValue(String::class.java)
                        val img = bookSnapshot.child("img").getValue(String::class.java)
                        val writerName = bookSnapshot.child("writerName").getValue(String::class.java)
                        val introduction = bookSnapshot.child("introduction").getValue(String::class.java)
                        val category = bookSnapshot.child("category").getValue(String::class.java)
                        if (nameBook != null && img != null && writerName != null && introduction!= null) {
                            val book = BooksModel(nameBook, writerName, img, introduction, category)
                            listBook.add(book)
                        }
                    }
                    allBookAdapter.notifyItemInserted(listBook.size)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Database Error: $error")
                }
            })
    }


    private fun setListeners() {
        binding.imgBackHomeProfile.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
    }

    override fun onClickBook(position: Int) {
        val selectedBook = listBook[position]
        val bundle = Bundle()
        bundle.putSerializable("selectedBook", selectedBook)
        NavHostFragment.findNavController(this).navigate(R.id.action_allBookFragment_to_bookIntroductionFragment, bundle)
    }

}
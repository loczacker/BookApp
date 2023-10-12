package com.zacker.bookapp.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.FragmentSearchBookBinding
import com.zacker.bookapp.model.BooksModel
import java.util.ArrayList


class SearchBookFragment : Fragment(), BookSearchAdapter.OnBookItemClickListener {

    private lateinit var binding: FragmentSearchBookBinding
    private val listSearchBook: ArrayList<BooksModel> = arrayListOf()
    private lateinit var bookSearchAdapter: BookSearchAdapter
    private var databaseListener: ValueEventListener? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setupRecyclerView()
        searchBook()
    }

    private fun setupRecyclerView() {
        bookSearchAdapter = BookSearchAdapter(listSearchBook, this)
        binding.rvSearch.adapter = bookSearchAdapter
    }


    private fun searchBook() {
        buttonListener()
        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listSearchBook.clear()
            }

            override fun afterTextChanged(s: Editable?) {
                val searchQuery = s.toString()
                if (searchQuery.isNotEmpty()) {
                    filter(searchQuery)
                }
            }
        })
    }

    private fun filter(searchQuery: String) {
        listSearchBook.clear()

        if (databaseListener != null) {
            val database = FirebaseDatabase.getInstance()
            val reference = database.reference.child("Books")
            reference.removeEventListener(databaseListener!!)
        }

        val database = FirebaseDatabase.getInstance()
        val reference = database.reference.child("Books")

        databaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (bookSnapshot in snapshot.children) {
                    val nameBook = bookSnapshot.child("nameBook").getValue(String::class.java)
                    val img = bookSnapshot.child("img").getValue(String::class.java)
                    val writerName = bookSnapshot.child("writerName").getValue(String::class.java)
                    val introduction = bookSnapshot.child("introduction").getValue(String::class.java)
                    val category = bookSnapshot.child("category").getValue(String::class.java)

                    if (nameBook != null && img != null && writerName != null && introduction != null) {
                        val book = BooksModel(nameBook, writerName, img, introduction, category)
                        if (nameBook.contains(searchQuery)) {
                            listSearchBook.add(book)
                        }
                    }
                }

                // Update your RecyclerView adapter here
                bookSearchAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        reference.addValueEventListener(databaseListener as ValueEventListener)
    }



    private fun buttonListener() {

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listSearchBook.clear()
                bookSearchAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {
                val searchQuery = s.toString()
                if (searchQuery.isNotEmpty()) {
                    filter(searchQuery)
                }
            }
        })

    }


    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun setListener() {
        binding.ibBack.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
    }

    override fun onClickBook(position: Int) {
        val selectedBook = listSearchBook[position]
        val bundle = Bundle()
        bundle.putSerializable("selectedBook", selectedBook)
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_bookIntroductionFragment, bundle)
    }
}
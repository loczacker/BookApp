package com.zacker.bookapp.ui.homeBookCase.booksViewed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.FragmentBooksViewedBinding
import com.zacker.bookapp.model.BooksModel
import com.zacker.bookapp.ui.homeBookCase.favourite.FavouriteAdapter
import java.util.ArrayList

class BooksViewedFragment : Fragment(), BookViewedAdapter.OnBookItemClickListener{

    private lateinit var binding: FragmentBooksViewedBinding
    private val listViewedBook: ArrayList<BooksModel> = arrayListOf()
    private lateinit var booksViewedAdapter: BookViewedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBooksViewedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    private fun setAdapter() {
        booksViewedAdapter = BookViewedAdapter(listViewedBook, this)
        binding.recyclerViewTabViewed.adapter = booksViewedAdapter
        val database = FirebaseDatabase.getInstance()
        var uidUser = FirebaseAuth.getInstance().uid
        database.reference.child("Users").child(uidUser.toString())
            .child("viewedBook").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val viewedBooks: MutableList<BooksModel> = mutableListOf()

                    for (childSnapshot in snapshot.children) {
                        val nameBook = childSnapshot.key

                        if (nameBook != null) {
                            database.reference.child("Books").child(nameBook)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(bookSnapshot: DataSnapshot) {
                                        val img = bookSnapshot.child("img").getValue(String::class.java)
                                        val writeName = bookSnapshot.child("writerName").getValue(String::class.java)
                                        val introduction = bookSnapshot.child("introduction").getValue(String::class.java)
                                        val category = bookSnapshot.child("category").getValue(String::class.java)

                                        if (img != null && writeName != null && category != null) {
                                            val book = BooksModel(nameBook, writeName, img, introduction, category)
                                            viewedBooks.add(book)
                                        }

                                        if (viewedBooks.size == snapshot.childrenCount.toInt()) {
                                            listViewedBook.clear()
                                            listViewedBook.addAll(viewedBooks)
                                            listViewedBook.sortBy { it.nameBook }
                                            booksViewedAdapter.notifyDataSetChanged()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    override fun onClickBook(position: Int) {
        val selectedBook = listViewedBook[position]
        val bundle = Bundle()
        bundle.putSerializable("selectedBook", selectedBook)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_homeFragment_to_bookIntroductionFragment, bundle)
    }
}
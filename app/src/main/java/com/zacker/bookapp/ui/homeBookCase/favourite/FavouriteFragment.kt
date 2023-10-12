package com.zacker.bookapp.ui.homeBookCase.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.FragmentFavouriteBinding
import com.zacker.bookapp.model.BooksModel
import java.util.ArrayList

class FavouriteFragment : Fragment(), FavouriteAdapter.OnBookItemClickListener,
    FavouriteAdapter.DeleteItemFavourite {
    private lateinit var binding: FragmentFavouriteBinding
    private val listFavouriteBook: ArrayList<BooksModel> = arrayListOf()
    private lateinit var favouriteAdapter: FavouriteAdapter
    private val database = FirebaseDatabase.getInstance()
    private val uidUser = FirebaseAuth.getInstance().uid

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?):
            View {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    private fun setAdapter() {
        favouriteAdapter = FavouriteAdapter(listFavouriteBook, this, this)
        binding.recyclerViewTabFavourite.adapter = favouriteAdapter

        database.reference.child("Users").child(uidUser.toString())
            .child("likeBook").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newFavouriteBooks: MutableList<BooksModel> = mutableListOf()

                    for (childSnapshot in snapshot.children) {
                        val nameBook = childSnapshot.key

                        if (nameBook != null) {
                            database.reference.child("Books").child(nameBook)
                                .addListenerForSingleValueEvent(object : ValueEventListener { // Đổi sang `addListenerForSingleValueEvent`
                                    override fun onDataChange(bookSnapshot: DataSnapshot) {
                                        val img = bookSnapshot.child("img").getValue(String::class.java)
                                        val writeName = bookSnapshot.child("writerName").getValue(String::class.java)
                                        val introduction = bookSnapshot.child("introduction").getValue(String::class.java)
                                        val category = bookSnapshot.child("category").getValue(String::class.java)

                                        if (img != null && writeName != null && category != null) {
                                            val book = BooksModel(nameBook, writeName, img, introduction, category)
                                            newFavouriteBooks.add(book)
                                        }

                                        if (newFavouriteBooks.size == snapshot.childrenCount.toInt()) {
                                            listFavouriteBook.clear()
                                            listFavouriteBook.addAll(newFavouriteBooks)
                                            listFavouriteBook.sortBy { it.nameBook }
                                            favouriteAdapter.notifyDataSetChanged()
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

    override fun onClickCancel(book: BooksModel) {
        val nameBook = book.nameBook ?: ""
        val likeBookRef = database.reference.child("Users").child(uidUser.toString()).child("likeBook")
        likeBookRef.child(nameBook).removeValue()
            .addOnSuccessListener {
                // Remove the book from the local list
                val removed = listFavouriteBook.remove(book)
                if (removed) {
                    favouriteAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(activity, "Không tìm thấy sách để xóa", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Xóa không thành công: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onClickBook(position: Int, book: BooksModel) {
        val selectedBook = listFavouriteBook[position]
        val bundle = Bundle()
        bundle.putSerializable("selectedBook", selectedBook)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_homeFragment_to_bookIntroductionFragment, bundle)
    }
    }


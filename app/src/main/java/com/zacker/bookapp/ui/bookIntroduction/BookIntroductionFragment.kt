package com.zacker.bookapp.ui.bookIntroduction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.FragmentBookIntroductionBinding
import com.zacker.bookapp.model.BooksModel

class BookIntroductionFragment : Fragment() {

    private lateinit var binding: FragmentBookIntroductionBinding
    private lateinit var viewModel: BookIntroductionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookIntroductionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[BookIntroductionViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        val args = arguments
        if (args != null) {
            val selectedBook = args.getSerializable("selectedBook") as? BooksModel
            if (selectedBook != null) {
                viewModel.setNameBook("${selectedBook?.nameBook}")
                binding.tvNameBook.text = selectedBook.nameBook
                binding.tvNameWriter.text = selectedBook.writerName
                binding.tvCategory.text = selectedBook.category
                binding.tvIntroduction.text = selectedBook.introduction
                Glide.with(binding.imgBook.context)
                    .load(selectedBook.img)
                    .into(binding.imgBook)
            }
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nameBook = viewModel.nameBook.value
        setUpObserver()
        setListener(nameBook)
        nameBook?.let { checkFavourite(it) }
    }

    private fun setListener(nameBook: String?) {
        binding.ibBack.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
        binding.btnRead.setOnClickListener {
            val bundleBook = Bundle()
            bundleBook.putString("nameBook", nameBook)
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_bookIntroductionFragment_to_chapFragment, bundleBook)
        }
        binding.btnLove.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val firebaseAuth = FirebaseAuth.getInstance()
            val nameBook = viewModel.nameBook.value.toString()
            val userUid = firebaseAuth.uid
            if (userUid != null) {
                val userLikeBookRef = database.reference.child("Users")
                    .child(userUid).child("likeBook").child(nameBook)
                val likeBookUpdates = HashMap<String, Any>()
                likeBookUpdates["Status"] = true
                userLikeBookRef.updateChildren(likeBookUpdates)
                    .addOnSuccessListener {
                        Toast.makeText(activity, "Đã thêm thành công $nameBook vào danh sách yêu thích ", Toast.LENGTH_SHORT).show()
                        checkFavourite(nameBook)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(activity, "Lỗi khi thêm $nameBook vào danh sách yêu thích: $e", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun checkFavourite(nameBook: String) {
        val database = FirebaseDatabase.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        val userUid = firebaseAuth.uid

        if (userUid != null) {
            val userRef = database.reference.child("Users").child(userUid)

            userRef.child("likeBook").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var isBookLiked = false
                    for (snapShot in snapshot.children) {
                        val nameBookCheck = snapShot.key
                        if (nameBook == nameBookCheck) {
                            isBookLiked = true
                            break
                        }
                    }
                    binding.btnLove.isVisible = !isBookLiked
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }




    private fun setUpObserver() {
        viewModel.nameBook.observe(viewLifecycleOwner) {
        }
    }

}
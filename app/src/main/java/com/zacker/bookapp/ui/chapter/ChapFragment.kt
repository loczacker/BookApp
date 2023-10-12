package com.zacker.bookapp.ui.chapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.FragmentChapBinding
import com.zacker.bookapp.model.ChapsModel
import com.zacker.bookapp.ui.bookIntroduction.BookIntroductionViewModel
import java.util.ArrayList


class ChapFragment : Fragment() , AllChapAdapter.OnBookItemClickListener{

    private lateinit var binding: FragmentChapBinding
    private lateinit var viewModel: BookIntroductionViewModel

    private val listChap: ArrayList<ChapsModel> = arrayListOf()
    private lateinit var allChapAdapter: AllChapAdapter

    private lateinit var nameBook: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChapBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[BookIntroductionViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameBook = arguments?.getString("nameBook").toString()
        showChapter(nameBook)
        setupRecyclerView()
        setUpObserver()
        setListener()
    }

    private fun setupRecyclerView() {
        allChapAdapter = AllChapAdapter(listChap, this)
        binding.recyclerViewAllChap.adapter = allChapAdapter
    }

    private fun showChapter(nameBook: String?) {
        val database = FirebaseDatabase.getInstance()
        database.reference.child("Books").child(nameBook.toString())
            .child("chapter").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listChap.clear()
                    val chaptersMap = mutableMapOf<String, String>()

                    for (chapSnapshot in snapshot.children) {
                        val nameChap = chapSnapshot.key
                        val content = chapSnapshot.child("content").getValue(String::class.java)
                        if (nameChap != null && content != null) {
                            chaptersMap[nameChap] = content
                        }
                    }

                    // Sắp xếp danh sách chương theo độ dài của tên chương
                    val sortedChapters = chaptersMap.toList().sortedWith(compareBy { it.first.length })

                    for (sortedChapter in sortedChapters) {
                        val chapter = ChapsModel(sortedChapter.first, sortedChapter.second)
                        listChap.add(chapter)
                    }

                    allChapAdapter.notifyItemInserted(listChap.size)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }


    private fun setListener() {
        binding.imgBack.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
    }

    private fun setUpObserver() {}

    override fun onClickBook(position: Int) {
        val selectedChap = listChap[position]
        val bundleChap = Bundle()
        bundleChap.putSerializable("selectedChap", selectedChap)
        val bundleBook = Bundle()
        bundleBook.putString("nameBook", nameBook)
        val combinedBundle = Bundle()
        combinedBundle.putBundle("bundleChap", bundleChap)
        combinedBundle.putBundle("bundleBook", bundleBook)
        NavHostFragment.findNavController(this).navigate(R.id.action_chapFragment_to_readBookFragment, combinedBundle)
        setViewedBook(nameBook)
    }

    private fun setViewedBook(nameBook: String) {
        val database = FirebaseDatabase.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        val userUid = firebaseAuth.uid
        if (userUid != null) {
            val userLikeBookRef = database.reference.child("Users")
                .child(userUid).child("viewedBook").child(this.nameBook)
            val viewedBookUpdates = HashMap<String, Any>()
            viewedBookUpdates["Status"] = true
            userLikeBookRef.updateChildren(viewedBookUpdates)
        }
    }
}
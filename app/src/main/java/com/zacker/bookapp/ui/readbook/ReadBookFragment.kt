package com.zacker.bookapp.ui.readbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zacker.bookapp.databinding.FragmentReadBookBinding
import com.zacker.bookapp.model.ChapsModel

class ReadBookFragment : Fragment() {

    private lateinit var binding: FragmentReadBookBinding
    private lateinit var viewModel: ReadBookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReadBookBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ReadBookViewModel::class.java]
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setUpBundle()
        return binding.root
    }

    private fun setUpBundle() {
        val bundleChap = requireArguments().getBundle("bundleChap")
        val bundleBook = requireArguments().getBundle("bundleBook")

        val selectedChap = bundleChap?.getSerializable("selectedChap") as? ChapsModel
        val nameBook = bundleBook?.getString("nameBook")
        if (nameBook!= null && selectedChap!= null) {
            showChapter(nameBook.toString(), selectedChap)
        }
    }

    private fun showChapter(nameBook: String, selectedChap: ChapsModel?) {
        val database = FirebaseDatabase.getInstance()
        database.reference.child("Books").child(nameBook)
            .child("chapter").child(selectedChap?.chapter.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                        val nameChap = snapshot.child("chapter").getValue(String::class.java)
                        val content = snapshot.child("content").getValue(String::class.java)
                        viewModel.setNameChap("$nameChap")
                        viewModel.setContent("$content")
                        if (nameChap != null && content != null) {
                            viewModel.setNameChap("$nameChap")
                            viewModel.setContent("$content")
//                            binding.tvChap.text = viewModel.nameChap.value
//                            binding.tvContent.text = viewModel.content.value
                        }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }

    private fun setListener() {
        binding.ibBack.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
    }
}
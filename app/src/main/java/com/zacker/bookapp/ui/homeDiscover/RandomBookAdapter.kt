package com.zacker.bookapp.ui.homeDiscover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.ItemBookDiscoverDetailBinding
import com.zacker.bookapp.databinding.ItemBookDiscoverRandomBinding
import com.zacker.bookapp.model.BooksModel

class RandomBookAdapter(
    private val books: List<BooksModel>,
    private val callback: OnBookItemClickListener
) : RecyclerView.Adapter<RandomBookAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemBookDiscoverRandomBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(book: BooksModel) {
                binding.tvNameBook.text = book.nameBook
                binding.tvIntroduction.text = book.introduction
                Glide.with(binding.imgBook.context)
                    .load(book.img)
                    .placeholder(R.drawable.profile)
                    .into(binding.imgBook)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookDiscoverRandomBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        books[position].let {
            holder.bind(it)
        }
        holder.itemView.setOnClickListener {
            callback.onClickBook(position)
        }
    }

    interface OnBookItemClickListener{
        fun onClickBook(position: Int)
    }
}
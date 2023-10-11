package com.zacker.bookapp.ui.homeBookCase.booksViewed

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.ItemBookDiscoverDetailBinding
import com.zacker.bookapp.model.BooksModel

class BookViewedAdapter(
    private val books: List<BooksModel>,
    private val callback: OnBookItemClickListener
) : RecyclerView.Adapter<BookViewedAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemBookDiscoverDetailBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(book: BooksModel) {
                binding.tvNameBook.text = book.nameBook
                binding.tvNameWriter.text = book.writerName
                binding.tvIntroduction.text = book.introduction
                Glide.with(binding.imgBook.context)
                    .load(book.img)
                    .into(binding.imgBook)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookDiscoverDetailBinding.inflate(
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
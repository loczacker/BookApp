package com.zacker.bookapp.ui.homeDiscover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zacker.bookapp.R
import com.zacker.bookapp.databinding.ItemBookDiscoverBinding
import com.zacker.bookapp.model.BooksModel

class NewBookAdapter(
    private val books: List<BooksModel>,
    private val callback: OnBookItemClickListener
) : RecyclerView.Adapter<NewBookAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemBookDiscoverBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(book: BooksModel) {
                binding.tvNameBook.text = book.nameBook
                Glide.with(binding.imgBook.context)
                    .load(book.img)
                    .placeholder(R.drawable.profile)
                    .into(binding.imgBook)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookDiscoverBinding.inflate(
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
            callback.onClick(position)
        }
    }

    interface OnBookItemClickListener{
        fun onClick(position: Int)
    }
}
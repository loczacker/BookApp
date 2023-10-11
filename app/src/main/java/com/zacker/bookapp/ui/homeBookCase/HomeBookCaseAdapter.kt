package com.zacker.bookapp.ui.homeBookCase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zacker.bookapp.databinding.ItemBookBinding
import com.zacker.bookapp.model.BooksModel

class HomeBookCaseAdapter(
    private val books: List<BooksModel>,
    private val callback: OnBookItemClickListener
) : RecyclerView.Adapter<HomeBookCaseAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemBookBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(book: BooksModel) {

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookBinding.inflate(
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
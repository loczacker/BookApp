package com.zacker.bookapp.ui.homeBookCase.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zacker.bookapp.databinding.ItemLoveBinding
import com.zacker.bookapp.model.BooksModel

class FavouriteAdapter(
    private val books: List<BooksModel>,
    private val callback: OnBookItemClickListener,
    private val callBackCancel: DeleteItemFavourite
) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemLoveBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(book: BooksModel, deleteCancel: DeleteItemFavourite) {
                binding.tvNameBook.text = book.nameBook
                binding.tvNameWriter.text = book.writerName
                binding.tvCategory.text = book.category
                Glide.with(binding.imgBook.context)
                    .load(book.img)
                    .into(binding.imgBook)
                binding.ivCancel.setOnClickListener {
                    deleteCancel.onClickCancel(book)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLoveBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]
        books[position].let {
            holder.bind(it, callBackCancel)
        }
        holder.itemView.setOnClickListener {
            callback.onClickBook(position, book)
        }
    }


    interface OnBookItemClickListener{
        fun onClickBook(position: Int, book: BooksModel)
    }

    interface DeleteItemFavourite{
        fun onClickCancel(book: BooksModel)
    }
}
package com.zacker.bookapp.ui.chapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zacker.bookapp.databinding.ItemChapterBinding
import com.zacker.bookapp.model.ChapsModel

class AllChapAdapter(
    private val books: List<ChapsModel>,
    private val callback: OnBookItemClickListener
) : RecyclerView.Adapter<AllChapAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemChapterBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(chap: ChapsModel) {
                binding.tvChap.text = chap.chapter
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChapterBinding.inflate(
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
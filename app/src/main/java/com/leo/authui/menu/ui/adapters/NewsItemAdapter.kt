package com.leo.authui.menu.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.leo.authui.databinding.ItemRecyclerNewsBinding
import com.leo.authui.menu.domain.News
import kotlin.properties.Delegates

class NewsItemAdapter() : RecyclerView.Adapter<NewsItemAdapter.ViewHolder>() {

    var items: List<News> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }

    var onClickListener : ((News) -> Unit )? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onClickListener)
    }


    fun setData(data: MutableList<News>){
        this.items = data
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val binding: ItemRecyclerNewsBinding) : RecyclerView.ViewHolder(binding.cardLayout) {

        internal fun bind(value: News, listener: ((News) -> Unit)?) {
            binding.txtName.text = value.title
            binding.txtDescription.text = value.content
            Glide.with(binding.root)
                .load(value.urlToImage)
                .centerCrop()
                .into(binding.imgNews)

            binding.cardLayout.setOnClickListener {
                listener?.invoke(value)
            }
        }
        internal fun getCardLayout(): CardView {
            return binding.cardLayout
        }
    }

}

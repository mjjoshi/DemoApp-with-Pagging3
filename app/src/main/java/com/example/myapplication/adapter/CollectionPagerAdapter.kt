package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.CollectionItemListBinding
import com.example.myapplication.interfaces.OnItemClickListener
import com.example.myapplication.model.collections.CollectionListResponseItem
import com.example.myapplication.utils.loadImage

class CollectionPagerAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<CollectionListResponseItem, CollectionPagerAdapter.ViewHolder>(
        CollectionComparator
    ) {


    object CollectionComparator : DiffUtil.ItemCallback<CollectionListResponseItem>() {
        override fun areItemsTheSame(
            oldItem: CollectionListResponseItem,
            newItem: CollectionListResponseItem
        ): Boolean {
            return oldItem.user.name == newItem.user.name
        }

        override fun areContentsTheSame(
            oldItem: CollectionListResponseItem,
            newItem: CollectionListResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = getItem(position)
        if (items != null) {
            holder.bind(items, listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CollectionItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    class ViewHolder constructor(private val binding: CollectionItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            items: CollectionListResponseItem,
            listeners: OnItemClickListener
        ) {
            binding.apply {
                txtUserName.text = items.user.name
                imgProfile.loadImage(items.user.profile_image.large)
            }
            itemView.setOnClickListener {
                listeners.onItemClick(item = items)
            }

        }
    }


}
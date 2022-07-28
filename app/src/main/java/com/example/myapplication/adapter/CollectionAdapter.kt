package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.CollectionItemListBinding
import com.example.myapplication.interfaces.OnItemClickListener
import com.example.myapplication.model.collections.CollectionListResponseItem
import com.example.myapplication.utils.loadImage

class CollectionAdapter(listener: OnItemClickListener) : RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {

    private val listeners = listener
    private var dataCollection = arrayListOf<CollectionListResponseItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<CollectionListResponseItem>) {
        dataCollection.addAll(list)
        notifyDataSetChanged()
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

    override fun getItemCount() = dataCollection.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataCollection[position]
        holder.bind(item, listeners)
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
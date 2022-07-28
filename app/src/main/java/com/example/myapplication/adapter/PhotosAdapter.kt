package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.CollectionItemListBinding
import com.example.myapplication.interfaces.OnItemClickPhotoListener
import com.example.myapplication.model.photoGrid.PhotoGrideResponseItem
import com.example.myapplication.utils.loadImage
import java.util.*
import kotlin.collections.ArrayList


class PhotosAdapter(private val dataPhotos: ArrayList<PhotoGrideResponseItem>,
                    listener: OnItemClickPhotoListener, ) :
    RecyclerView.Adapter<PhotosAdapter.ViewHolder>(),
    Filterable {

    private val listeners = listener
    //private var dataPhotos = ArrayList<PhotoGrideResponseItem>()

//    @SuppressLint("NotifyDataSetChanged")
//    fun setData(list: ArrayList<PhotoGrideResponseItem>) {
//        dataPhotos.addAll(list)
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CollectionItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = dataPhotos.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = dataPhotos[position]
        holder.bind(post, listeners)
    }

    class ViewHolder constructor(private val binding: CollectionItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            items: PhotoGrideResponseItem,
            listeners: OnItemClickPhotoListener
        ) {
            binding.apply {
                txtUserName.text = items.user.name
                imgProfile.loadImage(items.user.profile_image.large)
            }
            itemView.setOnClickListener {
                listeners.onItemphotoClick(item = items)
            }

        }
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<PhotoGrideResponseItem>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(dataPhotos)
            } else {
                for (item in dataPhotos) {
                    if (item.user.name.lowercase(Locale.getDefault()).startsWith(
                            constraint.toString()
                                .lowercase(Locale.getDefault())
                        )
                    ) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            val data = filterResults?.values as ArrayList<PhotoGrideResponseItem>
            dataPhotos.clear()
            dataPhotos.addAll(data)
            notifyDataSetChanged()
        }

    }

}
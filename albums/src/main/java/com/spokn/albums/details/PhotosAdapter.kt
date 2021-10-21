package com.spokn.albums.details

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spokn.albums.common.data.local.Photo
import com.spokn.albums.databinding.PhotoItemBinding
import com.squareup.picasso.Picasso

class PhotosAdapter(private val picasso: Picasso) :
        ListAdapter<Photo, PhotosAdapter.DetailsViewHolder>(PhotoDC()) {

    var interaction: Interaction? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DetailsViewHolder(
            PhotoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false), interaction
    )

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) = holder.bind(getItem(position))

    fun swapData(data: List<Photo>) {
        submitList(data.toMutableList())
    }

    inner class DetailsViewHolder(private val binding: PhotoItemBinding, private val interaction: Interaction?)
        : RecyclerView.ViewHolder(binding.root), OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val clicked = getItem(adapterPosition)
            interaction?.photoClicked(clicked,v as ImageView)
        }

        fun bind(item: Photo) = with(itemView) {
            picasso.load(item.thumbnailUrl).into(binding.ivImage)
        }
    }

    interface Interaction {
        fun photoClicked(model: Photo, imageView: ImageView)
    }

    private class PhotoDC : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(
                oldItem: Photo,
                newItem: Photo
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
                oldItem: Photo,
                newItem: Photo
        ) = oldItem == newItem
    }
}
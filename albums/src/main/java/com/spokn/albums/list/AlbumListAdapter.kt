package com.spokn.albums.list

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spokn.albums.common.data.local.Album
import com.spokn.albums.databinding.AlbumItemBinding

class AlbumListAdapter
    : ListAdapter<Album, AlbumListAdapter.ListViewHolder>(AlbumDC()) {

    var interaction: Interaction? = null

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = ListViewHolder(AlbumItemBinding.inflate(LayoutInflater.from(parent.context),parent,false), interaction)

    override fun onBindViewHolder(
            holder: ListViewHolder,
            position: Int
    ) = holder.bind(getItem(position))

    fun swapData(data: List<Album>) {
        submitList(data.toMutableList())
    }

    inner class ListViewHolder(
            private val binding: AlbumItemBinding,
            private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root), OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val clicked = getItem(adapterPosition)
            interaction?.albumClicked(clicked, binding.tvTitle)
        }

        fun bind(item: Album) = with(itemView) {
            binding.tvTitle.text = item.title

            //SharedItem transition
            ViewCompat.setTransitionName(binding.tvTitle, item.title)
        }
    }

    interface Interaction {
        fun albumClicked(
                album: Album,
                tvTitle: TextView)
    }

    private class AlbumDC : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Album, newItem: Album) = oldItem == newItem
    }
}
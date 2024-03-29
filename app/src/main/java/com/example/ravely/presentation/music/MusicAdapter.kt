package com.example.ravely.presentation.music

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ravely.databinding.ItemMusicBinding
import com.example.ravely.domain.model.MusicModel
import com.example.ravely.utils.formatDuration

class MusicAdapter : ListAdapter<MusicModel, MusicAdapter.MusicViewHolder>(MusicDiffCallback()) {

    var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = getItem(position)
        holder.bind(music)
    }

    inner class MusicViewHolder(private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(music: MusicModel) {
            with(binding) {
                Glide.with(itemView).load(music.album).into(ivImage)

                tvMusicName.text = music.title
                tvArtistName.text = if (music.artist == "<unknown>") "Unknown" else music.artist
                tvMusicDuration.text = formatDuration(music.duration)

                root.setOnClickListener {
                    onItemClick?.invoke(adapterPosition)
                }
            }
        }
    }
}

class MusicDiffCallback : DiffUtil.ItemCallback<MusicModel>() {
    override fun areItemsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean {
        return oldItem == newItem
    }
}
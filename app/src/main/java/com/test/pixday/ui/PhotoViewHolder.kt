package com.test.pixday.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.pixday.R
import com.test.pixday.databinding.PhotoViewItemBinding
import com.test.pixday.model.Photo

/**
 * The [PhotoViewHolder] class binds the view item
 * to its corresponding collected data of class [Photo].
 */
class PhotoViewHolder(private val binding: PhotoViewItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: Photo?) {
        if (photo == null) {
            //binding.imageView.setImageResource()
        } else {
            Picasso.get()
                .load(photo.assets.preview.url)
                .into(binding.imageView)
        }
    }

    companion object {
        fun create(parent: ViewGroup): PhotoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_view_item, parent, false)
            val binding = PhotoViewItemBinding.bind(view)
            return PhotoViewHolder(binding)
        }
    }
}
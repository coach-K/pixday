package com.test.pixday.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.test.pixday.model.Photo

/**
 * The [PhotosAdapter] class implements the
 * [PagingDataAdapter] class which consumes,
 * organizes and relates a collected/submitted data
 * to its corresponding [RecyclerView].
 */
class PhotosAdapter : PagingDataAdapter<Photo, PhotoViewHolder>(PHOTO_COMPARATOR) {
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoItem = getItem(position)
        if (photoItem != null) {
            holder.bind(photoItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder.create(parent)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem == newItem
        }
    }
}
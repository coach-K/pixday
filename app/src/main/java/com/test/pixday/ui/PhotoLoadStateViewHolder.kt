package com.test.pixday.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.test.pixday.R
import com.test.pixday.databinding.PhotosLoadStateFooterViewItemBinding

/**
 * The [PhotoLoadStateViewHolder] is responsible for
 * binding the view items to its corresponding data
 * such as [LoadState].
 */
class PhotoLoadStateViewHolder(
    private val binding: PhotosLoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): PhotoLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.photos_load_state_footer_view_item, parent, false)
            val binding = PhotosLoadStateFooterViewItemBinding.bind(view)
            return PhotoLoadStateViewHolder(binding, retry)
        }
    }
}
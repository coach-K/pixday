package com.test.pixday.ui

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

/**
 * The [PhotoLoadStateAdapter] goes in the header and footer
 * of the [PagingDataAdapter] and its responsible for affecting
 * the retry functionality on a network request error or timeout.
 */
class PhotoLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PhotoLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: PhotoLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PhotoLoadStateViewHolder {
        return PhotoLoadStateViewHolder.create(parent, retry)
    }
}
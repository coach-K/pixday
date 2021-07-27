package com.test.pixday.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.pixday.data.ShutterStockRepository

/**
 * The [ViewModelFactory] class is responsible for
 * creating a new instance of [SearchRepositoriesViewModel]
 * injecting a [ShutterStockRepository] to every instance.
 */
class ViewModelFactory(private val repository: ShutterStockRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchRepositoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchRepositoriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
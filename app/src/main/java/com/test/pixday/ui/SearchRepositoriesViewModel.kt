package com.test.pixday.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.test.pixday.data.ShutterStockRepository
import com.test.pixday.model.Photo
import kotlinx.coroutines.flow.Flow

/**
 * The [SearchRepositoriesViewModel] class is responsible for
 * persisting and exposing request response of type [Flow] interface with
 * a generic type [PagingData] and [Photo] to the UI [MainActivity] class
 */
class SearchRepositoriesViewModel(private val repository: ShutterStockRepository): ViewModel() {
    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<Photo>>? = null

    fun searchPhoto(queryString: String): Flow<PagingData<Photo>> {
        //We're preventing users from making the same query on multiple times.
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<Photo>> = repository.getSearchResultStream(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}
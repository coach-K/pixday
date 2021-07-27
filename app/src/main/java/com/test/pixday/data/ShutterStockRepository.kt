package com.test.pixday.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.test.pixday.api.ShutterStockService
import com.test.pixday.model.Photo
import kotlinx.coroutines.flow.Flow

/**
 * The [ShutterStockRepository] provides an repository
 * pattern implementation for providing data to its consumer class
 * such as ViewModels.
 */
class ShutterStockRepository(
    private val service: ShutterStockService
) {
    fun getSearchResultStream(query: String): Flow<PagingData<Photo>> {
        //We don't need a placeholders so we're setting it to false
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { ShutterStockPagingSource(service, query) }
        ).flow
    }

    companion object {
        //We're getting 30 photos per page to prevent querying the api often.
        const val NETWORK_PAGE_SIZE = 30
    }
}
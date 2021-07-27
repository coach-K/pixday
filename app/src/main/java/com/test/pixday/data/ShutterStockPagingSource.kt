package com.test.pixday.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.pixday.api.ShutterStockService
import com.test.pixday.data.ShutterStockRepository.Companion.NETWORK_PAGE_SIZE
import com.test.pixday.model.Photo
import retrofit2.HttpException
import java.io.IOException

private const val SHUTTER_STOCK_STARTING_PAGE_INDEX = 1

/**
 * The [ShutterStockPagingSource] is reponsible for loading
 * pages of data from the backend querying the [ShutterStockService]
 */
class ShutterStockPagingSource(private val service: ShutterStockService, private val query: String): PagingSource<Int, Photo>() {
    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: SHUTTER_STOCK_STARTING_PAGE_INDEX
        val apiQuery = query
        return try {
            val response = service.searchImages(apiQuery, position, params.loadSize)
            val photos = response.data
            val nextKey = if (photos.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = photos,
                prevKey = if (position == SHUTTER_STOCK_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}
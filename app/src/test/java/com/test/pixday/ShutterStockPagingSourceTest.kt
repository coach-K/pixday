package com.test.pixday

import androidx.paging.PagingSource
import com.test.pixday.data.ShutterStockPagingSource
import com.test.pixday.mock.FakeShutterStockService
import com.test.pixday.mock.PhotoFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShutterStockPagingSourceTest {

    @Test
    fun loadPagesFromPagingSource() = runBlocking {
        val photoFactory = PhotoFactory(arrayListOf(PhotoFactory.photo, PhotoFactory.photo2))
        val fakeShutterStockService = FakeShutterStockService(photoFactory)

        val pagingSource = ShutterStockPagingSource(fakeShutterStockService, "Dog")
        assertEquals(
            PagingSource.LoadResult.Page(
                data = photoFactory.photoList,
                null,
                1
            ),
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun loadEmptyPagesFromPagingSource() = runBlocking {
        val photoFactory = PhotoFactory()
        val fakeShutterStockService = FakeShutterStockService(photoFactory)

        val pagingSource = ShutterStockPagingSource(fakeShutterStockService, "Dog")
        assertEquals(
            PagingSource.LoadResult.Page(
                data = listOf(),
                null,
                null
            ),
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 0,
                    placeholdersEnabled = false
                )
            )
        )
    }
}
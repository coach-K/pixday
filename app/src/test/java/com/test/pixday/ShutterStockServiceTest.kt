package com.test.pixday

import com.test.pixday.api.PhotoResponse
import com.test.pixday.mock.FakeShutterStockService
import com.test.pixday.mock.PhotoFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShutterStockServiceTest {
    private val photoFactory = PhotoFactory(arrayListOf(PhotoFactory.photo, PhotoFactory.photo2))
    private val fakeShutterStockService = FakeShutterStockService(photoFactory)

    @Test
    fun loadPhotoResponseFromShutterStockService() = runBlocking {
        val photoResponse = PhotoResponse(listOf(PhotoFactory.photo, PhotoFactory.photo2))
        val fakePhotoResponse = fakeShutterStockService.searchImages("Dog", 1, 2)
        assertEquals(
            photoResponse.data.size,
            fakePhotoResponse.data.size
        )
    }

    @Test
    fun photoResponseFromShutterStockServiceContainsPhoto() = runBlocking {
        val photoResponse = PhotoResponse(photoFactory.photoList)
        val fakePhotoResponse = fakeShutterStockService.searchImages("Dog", 1, 2)
        assertEquals(
            photoResponse.data[0].id,
            fakePhotoResponse.data[0].id
        )
        assertEquals(
            photoResponse.data[0].assets.preview.url,
            fakePhotoResponse.data[0].assets.preview.url
        )
        assertEquals(
            photoResponse.data[0].assets.preview.height,
            fakePhotoResponse.data[0].assets.preview.height
        )
        assertEquals(
            photoResponse.data[0].assets.preview.width,
            fakePhotoResponse.data[0].assets.preview.width
        )
    }

    @Test
    fun loadEmptyPhotoResponseFromShutterStockService() = runBlocking {
        val photoFactory = PhotoFactory()
        val fakeShutterStockService = FakeShutterStockService(photoFactory)

        val photoResponse = PhotoResponse()

        val fakePhotoResponse = fakeShutterStockService.searchImages("Dog", 1, 2)
        assertEquals(
            photoResponse.data.isEmpty(),
            fakePhotoResponse.data.isEmpty()
        )
    }
}
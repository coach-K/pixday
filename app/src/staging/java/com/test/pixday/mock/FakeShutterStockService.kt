package com.test.pixday.mock

import com.test.pixday.api.PhotoResponse
import com.test.pixday.api.ShutterStockService

class FakeShutterStockService(private val photoFactory: PhotoFactory) : ShutterStockService {
    override suspend fun searchImages(query: String, page: Int, itemsPerPage: Int): PhotoResponse {
        return PhotoResponse(photoFactory.photoList)
    }
}
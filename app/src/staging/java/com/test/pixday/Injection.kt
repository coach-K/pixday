package com.test.pixday

import androidx.lifecycle.ViewModelProvider
import com.test.pixday.data.ShutterStockRepository
import com.test.pixday.mock.FakeShutterStockService
import com.test.pixday.mock.PhotoFactory
import com.test.pixday.ui.ViewModelFactory

object Injection {
    private fun provideShutterStockRepository(): ShutterStockRepository {
        val photoFactory =
            PhotoFactory(arrayListOf(PhotoFactory.photo, PhotoFactory.photo2))
        val service = FakeShutterStockService(photoFactory)
        return ShutterStockRepository(service)
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideShutterStockRepository())
    }
}
package com.test.pixday

import androidx.lifecycle.ViewModelProvider
import com.test.pixday.api.ShutterStockService
import com.test.pixday.data.ShutterStockRepository
import com.test.pixday.ui.ViewModelFactory

private const val accessToken = "SHUTTER_STOCK_DEBUG_ACCESS_TOKEN_HERE"

object Injection {
    private fun provideShutterStockRepository(): ShutterStockRepository {
        return ShutterStockRepository(ShutterStockService.create(accessToken))
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideShutterStockRepository())
    }
}
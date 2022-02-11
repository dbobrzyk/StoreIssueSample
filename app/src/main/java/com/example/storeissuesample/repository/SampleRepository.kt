package com.example.storeissuesample.repository

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.fresh
import com.example.storeissuesample.CacheRefreshMode
import com.example.storeissuesample.service.LocaleService
import com.example.storeissuesample.service.SampleServiceImpl
import com.example.storeissuesample.stream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class SampleRepository {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val popularProductsService = SampleServiceImpl()
    private val localeService = LocaleService(applicationScope)

    private val popularProductsStore = StoreBuilder.fromFlow<Any, List<String>> {
        //This part does not run when called from refreshPopularProducts()
        Timber.d("PopularProducts / Store: start fun")
        localeService.getCurrentLocale()
            .map {
                Timber.d("PopularProducts / Store: get popular products after getting locale")
                popularProductsService.getPopularProducts()
            }
    }.build()


    fun getPopularProducts(): Flow<RepositoryData<List<String>>> {
        Timber.d("PopularProducts / Get flow to observe on")
        return popularProductsStore
            .stream("TEST_KEY_1", CacheRefreshMode.Refresh)
            .mapToRepositoryData()

    }

    suspend fun refreshPopularProducts(): Unit {
        Timber.d("PopularProducts / Refresh products from Repository")
        popularProductsStore.fresh("TEST_KEY_1")
        return Unit
    }
}

inline fun <Key : Any, Output : Any> StoreBuilder.Companion.fromFlow(
    noinline flowFactory: (Key) -> Flow<Output>
) = from(Fetcher.ofFlow(flowFactory))
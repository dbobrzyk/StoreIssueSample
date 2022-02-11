package com.example.storeissuesample

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.flow.Flow

internal fun <KEY : Any, OUTPUT : Any> Store<KEY, OUTPUT>.stream(
    query: KEY,
    refreshMode: CacheRefreshMode
): Flow<StoreResponse<OUTPUT>> {
    return when (refreshMode) {
        CacheRefreshMode.Cache -> stream(StoreRequest.cached(query, false))
        CacheRefreshMode.Refresh -> stream(StoreRequest.cached(query, true))
        CacheRefreshMode.Fresh -> stream(StoreRequest.fresh(query))
    }
}

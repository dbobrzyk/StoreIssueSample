package com.example.storeissuesample.repository

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed class RepositoryData<out T> {

    abstract val origin: DataOrigin

    data class Loading(
        override val origin: DataOrigin
    ) : RepositoryData<Nothing>()

    data class Data<T>(
        override val origin: DataOrigin,
        val data: T
    ) : RepositoryData<T>()

    data class NoNewData(override val origin: DataOrigin) : RepositoryData<Nothing>()

    data class Error(
        override val origin: DataOrigin,
        val error: Throwable
    ) : RepositoryData<Nothing>()
}

internal fun <T> Flow<StoreResponse<T>>.mapToRepositoryData(): Flow<RepositoryData<T>> {
    return map { response ->
        val origin = response.origin.toRepositoryEventOrigin()
        when (response) {
            is StoreResponse.Loading -> RepositoryData.Loading(
                origin = origin
            )
            is StoreResponse.Data -> RepositoryData.Data(
                origin = origin,
                data = response.value
            )
            is StoreResponse.NoNewData -> RepositoryData.NoNewData(origin)
            is StoreResponse.Error.Exception -> RepositoryData.Error(
                origin = origin,
                error = response.error
            )
            is StoreResponse.Error.Message -> {
                throw RuntimeException("Use exception instead")
            }
        }
    }
}

enum class DataOrigin {
    Cache,
    SourceOfTruth,
    Fetcher;

    fun isLocal(): Boolean {
        return when (this) {
            Cache,
            SourceOfTruth -> true
            Fetcher -> false
        }
    }
}

private fun ResponseOrigin.toRepositoryEventOrigin(): DataOrigin {
    return when (this) {
        ResponseOrigin.Cache -> DataOrigin.Cache
        ResponseOrigin.SourceOfTruth -> DataOrigin.SourceOfTruth
        ResponseOrigin.Fetcher -> DataOrigin.Fetcher
    }
}

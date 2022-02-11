package com.example.storeissuesample.main

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeissuesample.repository.RepositoryData
import com.example.storeissuesample.repository.SampleRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel() : ViewModel() {

    private val sampleRepository = SampleRepository()
    private val _viewState: MutableStateFlow<ShopViewState> = MutableStateFlow(ShopViewState())
    val viewState: StateFlow<ShopViewState> = _viewState.asStateFlow()

    init {
        observePopularProducts()
    }

    private fun observePopularProducts() {
        sampleRepository.getPopularProducts().onEach { event ->
            Timber.d("PopularProducts / Event observed: $event")
            when (event) {
                is RepositoryData.Data -> _viewState.update {
                    Timber.d("PopularProducts / Data loaded")
                    it.copy(
                        isRefreshing = false,
                        productsData = event.data
                    )
                }
                is RepositoryData.Error ->
                    _viewState.update {
                        it.copy(
                            isRefreshing = false,
                            productsData = null
                        )
                    }
                is RepositoryData.Loading -> _viewState.update {
                    Timber.d("PopularProducts / Data loading")
                    it.copy(isRefreshing = true)
                }
                is RepositoryData.NoNewData -> _viewState.update { it.copy(isRefreshing = false) }
            }
        }.launchIn(viewModelScope)
    }

    @ExperimentalStdlibApi
    fun onRefresh() {
        Timber.d("PopularProducts / On refresh started from VM")
        _viewState.update { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            buildList {
                add(async { sampleRepository.refreshPopularProducts() })
            }.awaitAll()
        }

    }


}

@Immutable
data class ShopViewState(
    val isRefreshing: Boolean = false,
    val productsData: List<String>? = null
)

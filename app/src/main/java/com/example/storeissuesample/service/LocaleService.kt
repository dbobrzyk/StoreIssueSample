package com.example.storeissuesample.service

import com.example.storeissuesample.repository.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class LocaleService(
    private val coroutineScope: CoroutineScope
) {

    val settings = Settings()

    private val stateFlow: StateFlow<String> =
        settings.onChange("")
            .map {
                "Locale"
            }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = "Locale"
            )

    fun getCurrentLocale(): StateFlow<String> {
        return stateFlow
    }
}
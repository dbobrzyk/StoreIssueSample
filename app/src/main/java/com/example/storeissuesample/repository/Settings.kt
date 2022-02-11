package com.example.storeissuesample.repository

import android.content.SharedPreferences
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class Settings {

    fun onChange(key: String): Flow<Unit> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
            if (key == k) {
                trySend(Unit)
            }
        }
        awaitClose { this.cancel() }
    }
}
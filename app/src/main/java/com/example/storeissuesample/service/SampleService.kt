package com.example.storeissuesample.service

interface SampleService {
    suspend fun getPopularProducts(): List<String>
}
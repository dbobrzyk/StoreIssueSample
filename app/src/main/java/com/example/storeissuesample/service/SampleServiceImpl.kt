package com.example.storeissuesample.service

import kotlinx.coroutines.delay
import kotlin.random.Random

class SampleServiceImpl : SampleService {

    override suspend fun getPopularProducts(): List<String> {
        delay(Random.nextLong(3000, 5000))
        val list = mutableListOf<String>()
        val rand = Random.nextInt(3, 10)
        for (i in 1..rand) {
            list.add(i.toString())
        }
        return list
    }

}
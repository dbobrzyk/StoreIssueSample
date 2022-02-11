package com.example.storeissuesample


enum class CacheRefreshMode {
    /*
     * Refresh mode - Cache, will return cached data if it was fetched before or it will fetch fresh one if not
     */
    Cache,

    /*
     * Refresh mode - Refresh, will emit firstly cached data if it was fetched before and then will emit fresh data
     */
    Refresh,

    /*
     * Refresh mode - Refresh, will emit only fresh data even if it was fetched before
     */
    Fresh
}
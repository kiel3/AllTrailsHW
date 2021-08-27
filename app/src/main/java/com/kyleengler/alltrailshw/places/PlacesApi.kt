package com.kyleengler.alltrailshw.places

import com.kyleengler.alltrailshw.entity.remote.NearbySearchResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface PlacesApi {
    @GET("nearbysearch/json?type=restaurant&radius=1500")
    suspend fun getRestaurantsByLocation(@Query("location") location: String): NearbySearchResponse
}
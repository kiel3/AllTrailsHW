package com.kyleengler.alltrailshw.places

import com.kyleengler.alltrailshw.entity.remote.NearbySearchResponse
import com.kyleengler.alltrailshw.entity.remote.TextSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {
    @GET("nearbysearch/json?type=restaurant&radius=1500")
    suspend fun getRestaurantsByLocation(@Query("location") location: String): NearbySearchResponse

    @GET("findplacefromtext/json?type=restaurant&radius=1500&inputtype=textquery&fields=name,rating,geometry,user_ratings_total,price_level,photos,place_id,types")
    suspend fun getRestaurantsByName(@Query("input") input: String): TextSearchResponse
}
package com.kyleengler.alltrailshw.entity.remote


import com.google.gson.annotations.SerializedName
import com.kyleengler.alltrailshw.model.RestaurantModel

data class TextSearchResponse(
    @SerializedName("candidates")
    val candidates: List<Candidate>?,
    @SerializedName("status")
    val status: String?
)

data class Candidate(
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("geometry")
    val geometry: Geometry,
    @SerializedName("name")
    val name: String,
    @SerializedName("photos")
    val photos: List<Photo>?,
    @SerializedName("price_level")
    val priceLevel: Int,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int,
    @SerializedName("types")
    val types: List<String>?
)

fun Candidate.toModel(): RestaurantModel = RestaurantModel(
    lat = geometry.location.lat,
    lng = geometry.location.lng,
    placeId = placeId,
    name = name,
    priceLevel = priceLevel,
    vicinity = "",
    rating = rating,
    userRatingsTotal = userRatingsTotal,
    pictureId = photos?.firstOrNull()?.photoReference,
    type = types?.firstOrNull()
)


package com.kyleengler.alltrailshw.entity.remote


import com.google.gson.annotations.SerializedName
import com.kyleengler.alltrailshw.model.RestaurantModel

data class Result(
    @SerializedName("business_status")
    val businessStatus: String?,
    @SerializedName("geometry")
    val geometry: Geometry,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("icon_background_color")
    val iconBackgroundColor: String?,
    @SerializedName("icon_mask_base_uri")
    val iconMaskBaseUri: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("opening_hours")
    val openingHours: OpeningHours?,
    @SerializedName("photos")
    val photos: List<Photo>?,
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("plus_code")
    val plusCode: PlusCode?,
    @SerializedName("price_level")
    val priceLevel: Int,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("reference")
    val reference: String?,
    @SerializedName("scope")
    val scope: String?,
    @SerializedName("types")
    val types: List<String>?,
    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int,
    @SerializedName("vicinity")
    val vicinity: String
)

fun Result.toModel(): RestaurantModel = RestaurantModel(
    lat = geometry.location.lat,
    lng = geometry.location.lng,
    placeId = placeId,
    name = name,
    priceLevel = priceLevel,
    vicinity = vicinity,
    rating = rating,
    userRatingsTotal = userRatingsTotal,
    pictureId = photos?.firstOrNull()?.photoReference,
    type = types?.firstOrNull()
)
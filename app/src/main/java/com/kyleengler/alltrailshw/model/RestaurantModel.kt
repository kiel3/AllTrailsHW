package com.kyleengler.alltrailshw.model

import com.google.android.gms.maps.model.LatLng

data class RestaurantModel(
    val lat: Double,
    val lng: Double,
    val placeId: String,
    val name: String,
    val priceLevel: Int,
    val vicinity: String,
    val rating: Double,
    val userRatingsTotal: Int,
    val pictureId: String?,
    val type: String?
) {

    val formatPriceLevel: String?
        get() {
            return if (priceLevel == 0) {
                null
            } else {
                val sb = StringBuilder()
                for (i in 0 until priceLevel) {
                    sb.append("$")
                }
                sb.toString()
            }
        }
    val latLng: LatLng
        get() = LatLng(lat, lng)

    fun getPhotoUrl(key: String): String? {
        return if (pictureId == null) {
            null
        } else {
            "https://maps.googleapis.com/maps/api/place/photo?photo_reference=$pictureId&maxwidth=100&key=$key"
        }
    }
}
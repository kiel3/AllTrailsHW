package com.kyleengler.alltrailshw.entity.remote


import com.google.gson.annotations.SerializedName

data class OpeningHours(
    @SerializedName("open_now")
    val openNow: Boolean?
)
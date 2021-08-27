package com.kyleengler.alltrailshw

import android.location.Location

fun Location.toQuery(): String {
    return "$latitude,$longitude"
}
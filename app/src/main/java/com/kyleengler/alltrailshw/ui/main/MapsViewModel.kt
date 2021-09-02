package com.kyleengler.alltrailshw.ui.main

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.MarkerOptions
import com.kyleengler.alltrailshw.RestaurantRepository
import com.kyleengler.alltrailshw.model.RestaurantModel
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class MapsViewModel
@Inject
constructor(
    private val restaurantRepository: RestaurantRepository
) {
    val mapMarkers: LiveData<List<RestaurantModel>?> = restaurantRepository.restaurantLiveData
    val userLocation: Location?
        get() = restaurantRepository.userLocation

}
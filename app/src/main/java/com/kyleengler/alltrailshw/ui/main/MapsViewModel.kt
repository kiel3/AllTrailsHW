package com.kyleengler.alltrailshw.ui.main

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kyleengler.alltrailshw.RestaurantRepository
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
import com.kyleengler.alltrailshw.entity.remote.Result
import com.kyleengler.alltrailshw.entity.remote.toModel
import com.kyleengler.alltrailshw.model.RestaurantModel

@FragmentScoped
class MapsViewModel
@Inject
constructor(
    private val restaurantRepository: RestaurantRepository
){
    val mapMarkers: LiveData<List<RestaurantModel>?>
    val userLocation: Location?
        get() = restaurantRepository.userLocation
    private val _mapMarkers = MutableLiveData<List<MarkerOptions>?>()

    init {
        mapMarkers = restaurantRepository.restaurantLiveData
    }
}
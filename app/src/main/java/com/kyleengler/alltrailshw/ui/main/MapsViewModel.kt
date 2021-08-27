package com.kyleengler.alltrailshw.ui.main

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

@FragmentScoped
class MapsViewModel
@Inject
constructor(
    private val restaurantRepository: RestaurantRepository
){
    val mapMarkers: LiveData<List<Result>>
    private val _mapMarkers = MutableLiveData<List<MarkerOptions>>()

    init {
        mapMarkers = restaurantRepository.restaurantLiveData
//            .map { results ->
//            results.map { result ->
//                MarkerOptions().position(LatLng(result.geometry.location.lat, result.geometry.location.lng))
//                    .title(result.placeId)
//            }
//        }
    }
}
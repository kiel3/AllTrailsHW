package com.kyleengler.alltrailshw.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kyleengler.alltrailshw.RestaurantRepository
import com.kyleengler.alltrailshw.entity.remote.toModel
import com.kyleengler.alltrailshw.model.RestaurantModel
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class ListViewModel
@Inject
constructor(
    private val restaurantRepository: RestaurantRepository
) {
    val mapMarkers: LiveData<List<RestaurantModel>> = restaurantRepository.restaurantLiveData
        .map { results ->
            results.map { result ->
                result.toModel()
            }
        }
}

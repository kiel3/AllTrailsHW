package com.kyleengler.alltrailshw.ui.main

import androidx.lifecycle.LiveData
import com.kyleengler.alltrailshw.RestaurantRepository
import com.kyleengler.alltrailshw.model.RestaurantModel
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class ListViewModel
@Inject
constructor(
    private val restaurantRepository: RestaurantRepository
) {
    val onFavoriteClick: ((RestaurantModel) -> Unit) = { restaurantModel ->
        restaurantRepository.toggleFavorite(restaurantModel)
    }
    val mapMarkers: LiveData<List<RestaurantModel>?> = restaurantRepository.restaurantLiveData
}

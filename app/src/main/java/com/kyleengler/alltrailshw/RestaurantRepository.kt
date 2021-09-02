package com.kyleengler.alltrailshw

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kyleengler.alltrailshw.database.FavoriteRestaurantDao
import com.kyleengler.alltrailshw.entity.remote.toModel
import com.kyleengler.alltrailshw.model.RestaurantModel
import com.kyleengler.alltrailshw.model.toFavoriteEntity
import com.kyleengler.alltrailshw.places.PlacesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantRepository
@Inject
constructor(
    private val placesApi: PlacesApi,
    private val favoriteRestaurantDao: FavoriteRestaurantDao
) {
    var userLocation: Location? = null
    val restaurantLiveData: LiveData<List<RestaurantModel>?>
        get() = _restaurantLiveData
    private val TAG = "RestaurantRepository"
    private val scope = CoroutineScope(Dispatchers.IO)
    private val _restaurantLiveData = MutableLiveData<List<RestaurantModel>?>()

    fun searchByLocation() {
        val locationQuery = userLocation?.toQuery() ?: return
        scope.launch {
            try {
                val response = placesApi.getRestaurantsByLocation(locationQuery)
                val models = response.results?.map { it.toModel() }
                val favorites = favoriteRestaurantDao.getAll()
                val restaurants = models?.map { restaurantModel ->
                    restaurantModel.favorite =
                        favorites.find { it.id == restaurantModel.placeId } != null
                    restaurantModel
                }
                _restaurantLiveData.postValue(restaurants)
                Log.e(TAG, "Got response $restaurants")
            } catch (e: Exception) {
                Log.e(TAG, "Error from location search", e)
            }
        }
    }

    fun searchByText(text: String) {
        scope.launch {
            try {
                val response = placesApi.getRestaurantsByName(text)
                val models = response.candidates?.map { it.toModel() }
                val favorites = favoriteRestaurantDao.getAll()
                val restaurants = models?.map { restaurantModel ->
                    restaurantModel.favorite =
                        favorites.find { it.id == restaurantModel.placeId } != null
                    restaurantModel
                }
                _restaurantLiveData.postValue(restaurants)
                Log.e(TAG, "Got response $restaurants")
            } catch (e: Exception) {
                Log.e(TAG, "Error from location search", e)
            }
        }
    }

    fun toggleFavorite(restaurantModel: RestaurantModel) {
        if (restaurantModel.favorite) {
            removeFavorite(restaurantModel)
        } else {
            addFavorite(restaurantModel)
        }
    }

    private fun refreshData() {
        scope.launch {
            val favorites = favoriteRestaurantDao.getAll()
            val restaurants = _restaurantLiveData.value?.map { restaurantModel ->
                restaurantModel.favorite =
                    favorites.find { it.id == restaurantModel.placeId } != null
                restaurantModel
            }
            _restaurantLiveData.postValue(restaurants)
        }
    }

    private fun addFavorite(restaurantModel: RestaurantModel) {
        scope.launch {
            try {
                favoriteRestaurantDao.insert(restaurantModel.toFavoriteEntity())
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting favorite", e)
            }
            refreshData()
        }
    }

    private fun removeFavorite(restaurantModel: RestaurantModel) {
        scope.launch {
            try {
                favoriteRestaurantDao.delete(restaurantModel.toFavoriteEntity())
            } catch (e: Exception) {
                Log.e(TAG, "Error inserting favorite", e)
            }
            refreshData()
        }
    }
}

package com.kyleengler.alltrailshw

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kyleengler.alltrailshw.places.PlacesApi
import com.kyleengler.alltrailshw.entity.remote.Result
import com.kyleengler.alltrailshw.entity.remote.toModel
import com.kyleengler.alltrailshw.model.RestaurantModel
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantRepository
@Inject
constructor(
    private val placesApi: PlacesApi
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
                _restaurantLiveData.postValue(models)
                Log.e(TAG, "Got response $response")
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
                _restaurantLiveData.postValue(models)
                Log.e(TAG, "Got response $models")
            } catch (e: Exception) {
                Log.e(TAG, "Error from location search", e)
            }
        }
    }
}
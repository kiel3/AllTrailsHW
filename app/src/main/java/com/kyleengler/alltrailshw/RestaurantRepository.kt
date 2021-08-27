package com.kyleengler.alltrailshw

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kyleengler.alltrailshw.places.PlacesApi
import com.kyleengler.alltrailshw.entity.remote.Result
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
    val restaurantLiveData: LiveData<List<Result>>
        get() = _restaurantLiveData
    private val TAG = "RestaurantRepository"
    private val scope = CoroutineScope(Dispatchers.IO)
    private val _restaurantLiveData = MutableLiveData<List<Result>>()

    fun searchByLocation() {
        val locationQuery = userLocation?.toQuery() ?: return
        scope.launch {
            try {
                val response = placesApi.getRestaurantsByLocation(locationQuery)
                _restaurantLiveData.postValue(response.results)
                Log.e(TAG, "Got response $response")
            } catch (e: Exception) {
                Log.e(TAG, "Error from location search", e)
            }
        }
    }
}
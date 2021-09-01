package com.kyleengler.alltrailshw.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kyleengler.alltrailshw.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val state: SavedStateHandle,
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {
    val screenState: LiveData<ScreenValue>
        get() = _screenState
    private val _screenState: MutableLiveData<ScreenValue> =
        MutableLiveData(state["screen"] ?: ScreenValue.Map)

    fun onScreenToggleClick() {
        val screen = if (_screenState.value == ScreenValue.Map) {
            ScreenValue.List
        } else {
            ScreenValue.Map
        }
        state["screen"] = screen
        _screenState.value = screen
    }

    fun performSearch(text: String) {
        restaurantRepository.searchByText(text)
    }

    fun clearSearchText() {
        restaurantRepository.searchByLocation()
    }
}

enum class ScreenValue {
    Map,
    List
}
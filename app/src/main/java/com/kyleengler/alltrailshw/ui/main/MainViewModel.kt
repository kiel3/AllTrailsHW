package com.kyleengler.alltrailshw.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kyleengler.alltrailshw.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        val search = state.get("search") as String?
        if (!search.isNullOrEmpty()) {
            restaurantRepository.searchByText(search)
        }
    }

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
        state.set("search", text)
        restaurantRepository.searchByText(text)
    }

    fun clearSearchText() {
        state.set("search", null)
//        restaurantRepository.clearSearch()
        restaurantRepository.searchByLocation()
    }
}

enum class ScreenValue {
    Map,
    List
}
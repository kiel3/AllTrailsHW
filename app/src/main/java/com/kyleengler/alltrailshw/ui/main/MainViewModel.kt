package com.kyleengler.alltrailshw.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class MainViewModel
@Inject
constructor() : ViewModel() {
    val screenState: LiveData<ScreenValue>
        get() = _screenState
    private val _screenState: MutableLiveData<ScreenValue> = MutableLiveData(ScreenValue.Map)

    fun onScreenToggleClick() {
        if (_screenState.value == ScreenValue.Map) {
            _screenState.value = ScreenValue.List
        } else {
            _screenState.value = ScreenValue.Map
        }
    }
}

enum class ScreenValue {
    Map,
    List
}
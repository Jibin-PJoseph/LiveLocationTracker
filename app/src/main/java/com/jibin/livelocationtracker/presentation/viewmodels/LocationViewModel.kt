package com.jibin.livelocationtracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.jibin.livelocationtracker.domain.usecases.GetUserLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getUserLocationUseCase: GetUserLocationUseCase
): ViewModel(){
    private val _locationState = MutableStateFlow<LatLng?>(null)
    val locationState: StateFlow<LatLng?> = _locationState.asStateFlow()

    init {
        fetchUserLocation()
    }

    fun fetchUserLocation() {
        viewModelScope.launch {
            val userLocation = getUserLocationUseCase()
            _locationState.value = userLocation
        }
    }
}
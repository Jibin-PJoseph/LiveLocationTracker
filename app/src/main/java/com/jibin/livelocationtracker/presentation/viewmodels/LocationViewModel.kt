package com.jibin.livelocationtracker.presentation.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.jibin.livelocationtracker.data.location.GeocoderHelper
import com.jibin.livelocationtracker.domain.usecases.GetUserLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getUserLocationUseCase: GetUserLocationUseCase,
    private val geocoderHelper: GeocoderHelper
) : ViewModel() {

    private val _locationState = MutableStateFlow<LatLng?>(null)
    val locationState: StateFlow<LatLng?> = _locationState.asStateFlow()

    private val _taggedLocations = MutableStateFlow<List<Pair<LatLng, String>>>(emptyList())
    val taggedLocations = _taggedLocations.asStateFlow()

    init {
        fetchUserLocation()
    }

    fun fetchUserLocation() {
        viewModelScope.launch {
            val userLocation = getUserLocationUseCase()
            _locationState.value = userLocation
        }
    }

    fun tagLocation(latLng: LatLng) {
        viewModelScope.launch {
            val address = geocoderHelper.getAddressFromLocation(latLng)
            _taggedLocations.value = _taggedLocations.value + (latLng to address)
        }
    }


    fun removeTaggedLocation(latLng: LatLng) {
        viewModelScope.launch {
            val updatedLocations = _taggedLocations.value.toMutableList()
            updatedLocations.removeAll { it.first.latitude == latLng.latitude && it.first.longitude == latLng.longitude }
            _taggedLocations.value = updatedLocations
        }
    }

    fun getAddress(latLng: LatLng, callback: (String) -> Unit) {
        viewModelScope.launch {
            val address = geocoderHelper.getAddressFromLocation(latLng)
            callback(address)
        }
    }
}

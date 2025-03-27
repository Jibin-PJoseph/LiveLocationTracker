package com.jibin.livelocationtracker.domain.repository

import com.google.android.gms.maps.model.LatLng

interface LocationRepository {
    suspend fun getAddressFromCoordinates(lat: Double, lon: Double) : String
    suspend fun getCurrentLocation(): LatLng?
}
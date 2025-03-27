package com.jibin.livelocationtracker.domain.usecases

import com.google.android.gms.maps.model.LatLng
import com.jibin.livelocationtracker.domain.repository.LocationRepository
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): LatLng? {
        return locationRepository.getCurrentLocation()
    }
}
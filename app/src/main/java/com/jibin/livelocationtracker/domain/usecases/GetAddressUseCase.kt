package com.jibin.livelocationtracker.domain.usecases

import com.jibin.livelocationtracker.domain.repository.LocationRepository
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): String {
        return repository.getAddressFromCoordinates(lat, lon)
    }
}
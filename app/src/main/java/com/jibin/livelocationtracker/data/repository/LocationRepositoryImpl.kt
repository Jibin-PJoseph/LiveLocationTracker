package com.jibin.livelocationtracker.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.jibin.livelocationtracker.domain.repository.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) : LocationRepository {

    override suspend fun getAddressFromCoordinates(lat: Double, lon: Double): String {
        return "Mock Address for ($lat, $lon)"
    }

    override suspend fun getCurrentLocation(): LatLng? {
        return withContext(Dispatchers.IO) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                val location = fusedLocationProviderClient.lastLocation.await()
                location?.let { LatLng(it.latitude, it.longitude) }
            } else {
                null
            }
        }
    }
}
package com.jibin.livelocationtracker.data.location
import android.content.Context

import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeocoderHelper @Inject constructor(@ApplicationContext private val context: Context) {
    suspend fun getAddressFromLocation(latLng: LatLng): String {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown Location"
            } catch (e: Exception) {
                "Address not found"
            }
        }
    }
}

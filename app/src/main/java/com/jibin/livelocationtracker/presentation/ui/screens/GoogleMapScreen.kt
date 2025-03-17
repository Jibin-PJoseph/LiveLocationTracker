import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.jibin.livelocationtracker.presentation.ui.components.ConfirmTaggingDialog
import com.jibin.livelocationtracker.presentation.ui.components.RequestLocationPermission
import com.jibin.livelocationtracker.presentation.viewmodels.LocationViewModel
import kotlinx.coroutines.delay


@Composable
fun GoogleMapScreen(viewModel: LocationViewModel) {
    val userLocation by viewModel.locationState.collectAsState()
    val taggedLocations by viewModel.taggedLocations.collectAsState()
    val cameraPositionState = rememberCameraPositionState()

    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var showInfoWindow by remember { mutableStateOf(false) }

    val markerState = rememberMarkerState()

    LaunchedEffect(userLocation) {
        userLocation?.let {
            markerState.position = it // Update marker position
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, 15f),
                1000
            )
            showInfoWindow = true
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(isMyLocationEnabled = userLocation != null),
        uiSettings = MapUiSettings(zoomControlsEnabled = true),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            selectedLocation = latLng
        }
    ) {
        Marker(
            state = markerState,
            title = "You are here",
            snippet = "Current location",
            onClick = {
                showInfoWindow = !showInfoWindow
                true
            }
        )
        LaunchedEffect(showInfoWindow) {
            delay(500)
            if (showInfoWindow) {
                markerState.showInfoWindow()
            }
        }

        taggedLocations.forEach { latLng ->
            Marker(
                state = rememberMarkerState(position = latLng),
                title = "Tagged Location",
                snippet = "Tap to remove",
                onClick = {
                    viewModel.removeTaggedLocation(latLng)
                    true
                }
            )
        }
    }

    RequestLocationPermission(viewModel)

    selectedLocation?.let { latLng ->
        ConfirmTaggingDialog(
            latLng = latLng,
            onConfirm = {
                viewModel.tagLocation(latLng)
                selectedLocation = null
            },
            onDismiss = { selectedLocation = null }
        )
    }
}



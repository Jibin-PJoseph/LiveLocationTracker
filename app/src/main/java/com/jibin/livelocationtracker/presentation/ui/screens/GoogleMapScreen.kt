import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.jibin.livelocationtracker.presentation.ui.components.RequestLocationPermission
import com.jibin.livelocationtracker.presentation.viewmodels.LocationViewModel


@Composable
fun GoogleMapScreen(viewModel: LocationViewModel) {
    val location by viewModel.locationState.collectAsState()
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(location) {
        location?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, 15f),
                1000
            )
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(isMyLocationEnabled = location != null),
        uiSettings = MapUiSettings(zoomControlsEnabled = true),
        cameraPositionState = cameraPositionState
    )

    RequestLocationPermission(viewModel)
}


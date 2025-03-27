package com.jibin.livelocationtracker.presentation.ui.components
import android.Manifest
import androidx.compose.runtime.*
import com.google.accompanist.permissions.*
import com.jibin.livelocationtracker.presentation.viewmodels.LocationViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(viewModel: LocationViewModel) {
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            viewModel.fetchUserLocation()
        } else {
            permissionState.launchPermissionRequest()
        }
    }
}

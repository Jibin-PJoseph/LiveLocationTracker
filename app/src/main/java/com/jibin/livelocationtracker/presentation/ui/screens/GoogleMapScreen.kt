package com.jibin.livelocationtracker.presentation.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.jibin.livelocationtracker.presentation.ui.components.ConfirmLocationTaggingDialog
import com.jibin.livelocationtracker.presentation.ui.components.RequestLocationPermission
import com.jibin.livelocationtracker.presentation.viewmodels.LocationViewModel
import kotlinx.coroutines.delay

@Composable
fun GoogleMapScreen(viewModel: LocationViewModel) {
    val userLocation by viewModel.locationState.collectAsState()
    val taggedLocations by viewModel.taggedLocations.collectAsState()
    val cameraPositionState = rememberCameraPositionState()

    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var selectedAddress by remember { mutableStateOf("Fetching address...") }
    var locationToRemove by remember { mutableStateOf<LatLng?>(null) }
    var showInfoWindow by remember { mutableStateOf(false) }
    val markerState = rememberMarkerState()

    LaunchedEffect(userLocation) {
        userLocation?.let {
            markerState.position = it
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
            selectedAddress = "Fetching address..."  // Reset
            viewModel.getAddress(latLng) { address ->
                selectedAddress = address  // Update when fetched
            }
        }
    ) {
        Marker(
            state = markerState,
            title = "You are here",
            snippet = "Current location",
            onClick = { showInfoWindow = !showInfoWindow; true }
        )
        LaunchedEffect(showInfoWindow) {
            delay(500)
            if (showInfoWindow) markerState.showInfoWindow()
        }

        taggedLocations.forEach { (latLng, address) ->
            val taggedMarkerState = rememberMarkerState(position = latLng)
            var showTaggedInfoWindow by remember { mutableStateOf(false) }

            Marker(
                state = taggedMarkerState,
                title = "Tagged Location",
                snippet = address,
                onClick = { showTaggedInfoWindow = !showTaggedInfoWindow; true }
            )

            LaunchedEffect(showTaggedInfoWindow) {
                delay(500)
                if (showTaggedInfoWindow) taggedMarkerState.showInfoWindow()
            }
        }
    }

    RequestLocationPermission(viewModel)

    selectedLocation?.let {
        ConfirmLocationTaggingDialog(
            latLng = it,
            address = selectedAddress,  // Show the correct address
            onConfirm = {
                viewModel.tagLocation(it)
                selectedLocation = null
            },
            onDismiss = { selectedLocation = null }
        )
    }
}


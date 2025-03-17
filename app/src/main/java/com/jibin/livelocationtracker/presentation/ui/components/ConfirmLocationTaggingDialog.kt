package com.jibin.livelocationtracker.presentation.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.LatLng

@Composable
fun ConfirmTaggingDialog(
    latLng: LatLng,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Tagging") },
        text = { Text("Do you want to tag this location?\n(${latLng.latitude}, ${latLng.longitude})") },
        confirmButton = {
            Button(onClick = onConfirm) { Text("Tag Location") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

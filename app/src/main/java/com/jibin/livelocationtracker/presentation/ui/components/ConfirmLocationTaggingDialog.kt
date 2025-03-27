package com.jibin.livelocationtracker.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng

@Composable
fun ConfirmLocationTaggingDialog(
    latLng: LatLng,
    address: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Location Tagging") },
        text = {
            Column {
                Text(text = "Do you want to tag this location?")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Address: $address", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            }
        },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Confirm") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}


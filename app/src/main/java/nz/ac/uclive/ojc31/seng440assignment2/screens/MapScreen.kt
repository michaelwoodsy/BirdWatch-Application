package nz.ac.uclive.ojc31.seng440assignment2.screens

import android.content.Context
import android.location.LocationManager
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen() {
    val context = LocalContext.current

    val fineLocationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    val coarseLocationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val universityOfCanterbury = LatLng(-43.5225, 172.5794)
    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(universityOfCanterbury, 10f)
    }
    val uiSettings = remember {
        MapUiSettings()
    }
    var properties by remember {
        mutableStateOf(MapProperties(
            mapType = MapType.HYBRID,
            isMyLocationEnabled = true
        ))
    }

    when (fineLocationPermissionState.status) {
        is PermissionStatus.Granted -> {

        }
        is PermissionStatus.Denied -> {
            when (coarseLocationPermissionState.status) {
                is PermissionStatus.Granted -> {

                }
                is PermissionStatus.Denied -> {
                    properties = MapProperties(
                        mapType = MapType.HYBRID,
                        isMyLocationEnabled = false
                    )
                }
                else -> {}
            }
        }
        else -> {}
    }
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
        ) {
        }
    }
}
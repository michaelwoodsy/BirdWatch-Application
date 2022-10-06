package nz.ac.uclive.ojc31.seng440assignment2.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen() {
    val universityOfCanterbury = LatLng(-43.5225, 172.5794)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(universityOfCanterbury, 10f)
    }
    val uiSettings = remember {
//        MapUiSettings(myLocationButtonEnabled = true)
        MapUiSettings(

        )
    }
    val properties by remember {
//        mutableStateOf(MapProperties(isMyLocationEnabled = true))
        mutableStateOf(MapProperties(
            mapType = MapType.HYBRID,

        ))
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
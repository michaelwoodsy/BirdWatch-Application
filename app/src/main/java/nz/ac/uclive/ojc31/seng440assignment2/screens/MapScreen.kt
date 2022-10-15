package nz.ac.uclive.ojc31.seng440assignment2.screens

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.screens.home.ExtendedAddEntryButton
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.BirdHistoryViewModel

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navController: NavHostController,
    viewModel: BirdHistoryViewModel = hiltViewModel()

) {
    val context = LocalContext.current

    val fineLocationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    val coarseLocationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val universityOfCanterbury = LatLng(-43.5225, 172.5794)
    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(universityOfCanterbury, 15f)
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

    when (coarseLocationPermissionState.status) {
        is PermissionStatus.Granted -> {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    if (location != null) {
                        val currentPosition = LatLng(location.latitude, location.longitude)
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(currentPosition, 15f)
                    }
                }
        }
        is PermissionStatus.Denied -> {
            when (fineLocationPermissionState.status) {
                is PermissionStatus.Granted -> {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            if (location != null) {
                                val currentPosition = LatLng(location.latitude, location.longitude)
                                cameraPositionState.position = CameraPosition.fromLatLngZoom(currentPosition, 15f)
                            }
                        }
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
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
        ) {
            for (entry in viewModel.historyList.value) {
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            entry.lat,
                            entry.long
                        )
                    ),
                    title = "${entry.bird.comName} | ${entry.observedDate}",
                    draggable = false,
                )
            }
        }
        ExtendedAddEntryButton(navController)
    }
}
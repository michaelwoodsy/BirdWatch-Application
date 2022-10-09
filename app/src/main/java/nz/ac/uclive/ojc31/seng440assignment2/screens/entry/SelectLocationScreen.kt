package nz.ac.uclive.ojc31.seng440assignment2.screens.entry

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.AddEntryViewModel
import okio.IOException


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SelectLocationScreen(
    navController: NavHostController,
    viewModel: AddEntryViewModel,
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
    val cameraPositionState = rememberCameraPositionState {
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

    when (fineLocationPermissionState.status) {
        is PermissionStatus.Granted -> {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    if (location != null && viewModel.currentRegion.value == "") {
                        val currentPosition = LatLng(location.latitude, location.longitude)
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(currentPosition, 15f)
                    }
                }
        }
        is PermissionStatus.Denied -> {
            when (coarseLocationPermissionState.status) {
                is PermissionStatus.Granted -> {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            if (location != null && viewModel.currentRegion.value == "") {
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
    if (viewModel.currentRegion.value != "") {
        val currentPosition = LatLng(viewModel.currentLat.value, viewModel.currentLong.value)
        cameraPositionState.position = CameraPosition.fromLatLngZoom(currentPosition, 15f)
    }
    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            DoneButton(navController, cameraPositionState, viewModel)
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = properties,
                    uiSettings = uiSettings,
                ) {
                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                cameraPositionState.position.target.latitude,
                                cameraPositionState.position.target.longitude
                            )
                        ),
                        title = stringResource(R.string.bird_location_marker),
                        draggable = false,
                    )
                }

            }
        }
    }
}

@Composable
fun DoneButton(
    navController: NavHostController,
    cameraPositionState: CameraPositionState,
    viewModel: AddEntryViewModel,
) {
    val ctx = LocalContext.current
    val errorText = stringResource(R.string.failed_location)
    Button(
        modifier = Modifier
            .width(100.dp)
            .padding(5.dp),
        onClick = {
            try {
                val geoCoder = Geocoder(ctx)
                val addressList = geoCoder.getFromLocation(cameraPositionState.position.target.latitude,
                    cameraPositionState.position.target.longitude, 1)
                if (addressList.size > 0) {
                    val address = addressList[0]
                    viewModel.currentLat.value = cameraPositionState.position.target.latitude
                    viewModel.currentLong.value = cameraPositionState.position.target.longitude
                    if (address.locality != null) {
                        viewModel.currentRegion.value = address.locality
                    } else if (address.subAdminArea != null) {
                        viewModel.currentRegion.value = address.subAdminArea
                    } else if (address.adminArea != null) {
                        viewModel.currentRegion.value = address.adminArea
                    } else {
                        Toast.makeText(ctx, errorText, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(ctx, errorText, Toast.LENGTH_LONG).show()
            } catch (e: InterruptedException) {
                e.printStackTrace()
                Toast.makeText(ctx, errorText, Toast.LENGTH_LONG).show()

            }

            navController.popBackStack()
        },
    ) {
        Text(text = stringResource(R.string.done_button))
    }
}

package nz.ac.uclive.ojc31.seng440assignment2.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
            val historyList = viewModel.historyList.value
            for (entry in historyList) {
                MapMarker(
                    context = context,
                    state = MarkerState(
                        position = LatLng(
                            entry.lat,
                            entry.long
                        )
                    ),
                    title = "${entry.bird.comName} | ${entry.observedDate}",
                    iconResourceId = R.mipmap.ic_launcher_round,
                )
            }
        }
        AddButton(navController = navController, cameraPosition = cameraPositionState)
    }
}

@Composable
private fun AddButton(navController: NavHostController, cameraPosition: CameraPositionState) {
    ExtendedAddEntryButton(
        navController,
        lat = cameraPosition.position.target.latitude.toString(),
        long = cameraPosition.position.target.longitude.toString(),
    )
}

@Composable
fun MapMarker(
    context: Context,
    state: MarkerState,
    title: String,
    @DrawableRes iconResourceId: Int,
) {
    val icon = bitmapDescriptor(
        context, iconResourceId
    )

    MarkerInfoWindowContent(
        state = state,
        title = title,
        icon = icon,
        draggable = false,
    ) { marker ->
        // Implement the custom info window here
        Column {
            Text(marker.title ?: stringResource(R.string.default_marker))
        }
    }
}

fun bitmapDescriptor(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth / 2, drawable.intrinsicHeight / 2)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth / 2,
        drawable.intrinsicHeight / 2,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}
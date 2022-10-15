package nz.ac.uclive.ojc31.seng440assignment2.screens

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Location
import android.provider.MediaStore
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.compose.*
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.data.entries.EntryDTO
import nz.ac.uclive.ojc31.seng440assignment2.model.Entry
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
                    entry = entry
                )

//                Marker(
//                    state = MarkerState(
//                        position = LatLng(
//                            entry.lat,
//                            entry.long
//                        )
//                    ),
//                    title = "${entry.bird.comName} | ${entry.observedDate}",
//                    draggable = false,
//                )
            }
        }
        ExtendedAddEntryButton(navController)
    }
}

@Composable
fun MapMarker(
    context: Context,
    state: MarkerState,
    title: String,
    @DrawableRes iconResourceId: Int,
    entry: EntryDTO,
    topPadding: Dp = 20.dp,
    birdImageSize: Dp = 200.dp,
) {
    val icon = bitmapDescriptor(
        context, iconResourceId
    )
    val imageId = entry.imageId
    var birdImageUrl = "https://www.justcolor.net/kids/wp-content/uploads/sites/12/nggallery/birds/coloring-pages-for-children-birds-82448.jpg"

    if (imageId != null) {
        birdImageUrl = ContentUris.withAppendedId(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId.toLong()).toString()
    }

    MarkerInfoWindowContent(
        state = state,
        title = title,
        icon = icon,
        draggable = false,
    ) { marker ->
        // Implement the custom info window here
        Column {
            Text(marker.title ?: "Default Marker Title")
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
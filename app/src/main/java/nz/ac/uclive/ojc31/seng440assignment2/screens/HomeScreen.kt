package nz.ac.uclive.ojc31.seng440assignment2.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.*
import nz.ac.uclive.ojc31.seng440assignment2.graphs.SubScreen

@ExperimentalPermissionsApi
@Composable
fun HomeScreen(
    navController: NavHostController,
    permissions: List<String>,
    deniedMessage: String = "Give this app a permission to proceed. If it doesn't work, then you'll have to do it manually from the settings.",
    rationaleMessage: String = "To use this app's functionalities, you need to give us the permission.",
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    Scaffold(
        floatingActionButton = {
            ExtendedAddEntryButton(navController)
        },
        topBar = {HomeTopAppBar(navController)}
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            HandleRequests(
                multiplePermissionsState = multiplePermissionsState,
                deniedContent = { shouldShowRationale ->
                    PermissionDeniedContent(
                        deniedMessage = deniedMessage,
                        rationaleMessage = rationaleMessage,
                        shouldShowRationale = shouldShowRationale,
                        onRequestPermission = { multiplePermissionsState.launchMultiplePermissionRequest() }
                    )
                },
                content = {
                    Content(
                        text = "PERMISSION GRANTED!",
                        showButton = false
                    ) {}
                }
            )
        }
    }

}

@Composable
private fun HomeTopAppBar(navController: NavHostController) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        title = {Text("BirdWatch")},
        navigationIcon = {
            IconButton( onClick = { navController.navigate(SubScreen.Settings.route)}) {
                Icon(Icons.Filled.Settings, null)
            }
        }
    )
}

@ExperimentalPermissionsApi
@Composable
private fun HandleRequests(
    multiplePermissionsState: MultiplePermissionsState,
    deniedContent: @Composable (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    var shouldShowRationale by remember { mutableStateOf(false) }
    val result = multiplePermissionsState.permissions.all {
        shouldShowRationale = it.status.shouldShowRationale
        it.status == PermissionStatus.Granted
    }
    if (result) {
        content()
    } else {
        deniedContent(shouldShowRationale)
    }
}

@Composable
fun Content(text: String, showButton: Boolean = true, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(12.dp))
        if (showButton) {
            Button(onClick = onClick) {
                Text(text = "Request")
            }
        }
    }
}

@Composable
fun ExtendedAddEntryButton(
    navController: NavHostController,
    birdId: String = "default",
    birdName: String = "default",
    lat: String = "default",
    long: String = "default",
) {
    ExtendedFloatingActionButton(
        text = { Text(text = "Add Entry") },
        icon = { Icon(Icons.Filled.Add, "") },
        onClick = { navController.navigate(
            "add_entry_screen/${birdId}/${birdName}/${lat}/${long}",
        ) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.surface,
        modifier = Modifier.padding(8.dp),
    )
}

@ExperimentalPermissionsApi
@Composable
fun PermissionDeniedContent(
    deniedMessage: String,
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {
    if (shouldShowRationale) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Permission Request",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(rationaleMessage)
            },
            confirmButton = {
                Button(onClick = onRequestPermission) {
                    Text("Give Permission")
                }
            }
        )
    } else {
        Content(text = deniedMessage, onClick = onRequestPermission)
    }

}


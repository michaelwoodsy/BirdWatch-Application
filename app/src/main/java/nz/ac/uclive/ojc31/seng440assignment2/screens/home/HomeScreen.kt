package nz.ac.uclive.ojc31.seng440assignment2.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.graphs.SubScreen
import nz.ac.uclive.ojc31.seng440assignment2.ui.theme.TitleFont

@Composable
fun HomeScreen(
    navController: NavHostController,
) {
    val configuration = LocalConfiguration.current

    Scaffold(
        floatingActionButton = {
            ExtendedAddEntryButton(navController)
        },
        topBar = {HomeTopAppBar(navController)},
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontFamily = TitleFont,
                    fontSize = 64.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(16.dp)
                )
                when (configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.height(80.dp).width(320.dp).padding(16.dp)
                        ) {
                            Text(text = "View Challenges", fontSize = 20.sp)
                        }
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.height(80.dp).width(320.dp).padding(16.dp)
                        ) {
                            Text(text = "View Achievements", fontSize = 20.sp)
                        }
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.height(80.dp).width(320.dp).padding(16.dp)
                        ) {
                            Text(text = "View Statistics", fontSize = 20.sp)
                        }
                    }
                    else -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = { /*TODO*/ },
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(text = "View Challenges", fontSize = 16.sp)
                            }
                            Button(
                                onClick = { /*TODO*/ },
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(text = "View Achievements", fontSize = 16.sp)
                            }
                            Button(
                                onClick = { /*TODO*/ },
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(text = "View Statistics", fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
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


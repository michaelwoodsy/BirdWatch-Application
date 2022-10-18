package nz.ac.uclive.ojc31.seng440assignment2.screens.home

import android.content.res.Configuration
import android.net.Uri
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
                            onClick = {
                                navController.navigate(SubScreen.Challenges.route)
                                      },
                            modifier = Modifier
                                .height(80.dp)
                                .width(320.dp)
                                .padding(16.dp)
                        ) {
                            Text(text = stringResource(R.string.view_challenges_button), fontSize = 20.sp)
                        }
                        Button(
                            onClick = {
                                navController.navigate(SubScreen.Achievements.route)
                            },
                            modifier = Modifier
                                .height(80.dp)
                                .width(320.dp)
                                .padding(16.dp)
                        ) {
                            Text(text = stringResource(R.string.view_achievements_button), fontSize = 20.sp)
                        }
                        Button(
                            onClick = {
                                navController.navigate(SubScreen.Statistics.route)
                            },
                            modifier = Modifier
                                .height(80.dp)
                                .width(320.dp)
                                .padding(16.dp)
                        ) {
                            Text(text = stringResource(R.string.view_stats_button), fontSize = 20.sp)
                        }
                    }
                    else -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = {
                                    navController.navigate(SubScreen.Challenges.route)
                                },
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(text = stringResource(R.string.view_challenges_button), fontSize = 16.sp)
                            }
                            Button(
                                onClick = {
                                    navController.navigate(SubScreen.Achievements.route)
                                },
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(text = stringResource(R.string.view_achievements_button), fontSize = 16.sp)
                            }
                            Button(
                                onClick = {
                                    navController.navigate(SubScreen.Statistics.route)
                                },
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(text = stringResource(R.string.view_stats_button), fontSize = 16.sp)
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
        title = {Text(stringResource(R.string.app_name))},
        navigationIcon = {
            IconButton( onClick = {
                navController.navigate(SubScreen.Settings.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }) {
                Icon(Icons.Filled.Settings, null)
            }
        }
    )
}

@Composable
fun ExtendedAddEntryButton(
    navController: NavHostController,
    birdId: String? = null,
    birdName: String? = null,
    lat: String? = null,
    long: String? = null,
    challengeId: String? = null
) {


    val builder =  Uri.Builder()
    builder.authority("add_entry_screen")
    if (!birdId.isNullOrEmpty()) builder.appendQueryParameter(SubScreen.AddEntryDetails.birdId, birdId)
    if (!birdName.isNullOrEmpty()) builder.appendQueryParameter(SubScreen.AddEntryDetails.birdName, birdName)
    if (!lat.isNullOrEmpty()) builder.appendQueryParameter(SubScreen.AddEntryDetails.lat, lat)
    if (!long.isNullOrEmpty()) builder.appendQueryParameter(SubScreen.AddEntryDetails.long, long)
    if (!challengeId.isNullOrEmpty()) builder.appendQueryParameter(SubScreen.AddEntryDetails.challengeId, challengeId)
    val queryString = builder.build().toString().replace("//", "")

    ExtendedFloatingActionButton(
        text = { Text(text = stringResource(R.string.add_entry_button)) },
        icon = { Icon(Icons.Filled.Add, "") },
        onClick = { navController.navigate(
            queryString,
        ) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.surface,
        modifier = Modifier.padding(8.dp),
    )
}


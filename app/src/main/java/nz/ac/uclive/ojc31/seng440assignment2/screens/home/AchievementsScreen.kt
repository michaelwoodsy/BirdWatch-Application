package nz.ac.uclive.ojc31.seng440assignment2.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.model.Achievement
import nz.ac.uclive.ojc31.seng440assignment2.ui.theme.RobotoCondensed
import nz.ac.uclive.ojc31.seng440assignment2.ui.theme.TitleFont
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.AchievementsViewModel
import java.time.format.DateTimeFormatter

@Composable
fun AchievementsScreen(
    navController: NavHostController,
) {
    Scaffold(
        topBar = { TopSection(navController = navController) }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.achievements_title),
                    fontFamily = TitleFont,
                    fontSize = 64.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
                AchievementsList()
            }
        }
    }
}

@Composable
fun AchievementsList(
    viewModel: AchievementsViewModel = hiltViewModel()
) {
    val achievementList by remember { viewModel.achievementList }
    val isLoading by remember { viewModel.isLoading }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .align(Alignment.Center)
            )
        }
    } else {
        if (achievementList.isEmpty()) {
            NoAchievementDisplay()
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                val itemCount = achievementList.size
                items(itemCount) {
                    AchievementRow(rowIndex = it, achievements = achievementList)
                }
            }
        }

    }
}

@Composable
fun NoAchievementDisplay() {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Column(
                Modifier
                    .padding(30.dp)
                    .padding(top = 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painterResource(R.drawable.ic_baseline_sentiment_very_dissatisfied_24),
                    null,
                    Modifier.size(80.dp)
                )
                Text(
                    text = stringResource(R.string.no_achievements),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.no_achievements_tip))
                    },
                    textAlign = TextAlign.Center
                )
            }
        }
        else -> {
            Row(
                Modifier
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Column(
                    Modifier.padding(5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painterResource(R.drawable.ic_baseline_sentiment_very_dissatisfied_24),
                        null,
                        Modifier.size(65.dp)
                    )
                    Text(
                        text = stringResource(R.string.no_achievements),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(R.string.no_achievements_tip))
                        },
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun AchievementRow(
    rowIndex: Int,
    achievements: List<Achievement>,
) {
    val achievement = achievements[rowIndex]
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Row(
                Modifier
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colors.surface,
                                MaterialTheme.colors.surface
                            )
                        )
                    )
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (achievement.achievementType == "Entry Number") {
                    Icon(
                        painterResource(R.drawable.trophy),
                        null,
                        Modifier
                            .size(30.dp)
                            .align(Alignment.CenterVertically)
                    )
                } else {
                    Icon(
                        painterResource(R.drawable.trophy_award),
                        null,
                        Modifier
                            .size(30.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
                Column() {
                    Text(
                        text = achievement.achievementText,
                        fontFamily = RobotoCondensed,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = buildAnnotatedString {
                            withStyle(style= SpanStyle(fontWeight = FontWeight.Bold)) {
                                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                                append(achievement.receivedDate.format(formatter))
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        else -> {
            Row(
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colors.surface,
                                MaterialTheme.colors.surface
                            )
                        )
                    )
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (achievement.achievementType == "Entry Number") {
                    Icon(
                        painterResource(R.drawable.trophy),
                        null,
                        Modifier
                            .size(30.dp)
                            .align(Alignment.CenterVertically)
                    )
                } else {
                    Icon(
                        painterResource(R.drawable.trophy_award),
                        null,
                        Modifier
                            .size(30.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
                Column() {
                    Text(
                        text = achievement.achievementText,
                        fontFamily = RobotoCondensed,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = buildAnnotatedString {
                            withStyle(style= SpanStyle(fontWeight = FontWeight.Bold)) {
                                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                                append(achievement.receivedDate.format(formatter))
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TopSection(navController: NavHostController) {
    Box(
        Modifier.padding(bottom = 30.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}
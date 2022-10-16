package nz.ac.uclive.ojc31.seng440assignment2.repository

import kotlinx.coroutines.flow.Flow
import nz.ac.uclive.ojc31.seng440assignment2.data.achievements.AchievementDao
import nz.ac.uclive.ojc31.seng440assignment2.data.entries.EntryDao
import java.time.LocalDate

class StatisticsRepository (
    private val entryDao: EntryDao,
    private val achievementDao: AchievementDao
) {
    fun getEntryCount(): Flow<List<Int>> = entryDao.getEntryCount()
    fun getFirstEntry(): Flow<List<LocalDate>> = entryDao.getFirst()
    fun getMostRecentEntry(): Flow<List<LocalDate>> = entryDao.getMostRecent()
    fun getMostCommonBird(): Flow<List<Int>> = entryDao.getEntryCount()

    fun getAchievementsCount(): Flow<List<Int>> = achievementDao.getAchievementCount()



}
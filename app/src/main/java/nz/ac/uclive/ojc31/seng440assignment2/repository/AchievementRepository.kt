package nz.ac.uclive.ojc31.seng440assignment2.repository

import kotlinx.coroutines.flow.Flow
import nz.ac.uclive.ojc31.seng440assignment2.data.achievements.AchievementDao
import nz.ac.uclive.ojc31.seng440assignment2.model.Achievement

class AchievementRepository (
    private val achievementDao: AchievementDao
) {
    fun getAllFlow(): Flow<List<Achievement>> = achievementDao.getAllAchievements()
    suspend fun insert(achievement: Achievement) = achievementDao.insert(achievement = achievement)
    suspend fun update(achievement: Achievement) = achievementDao.update(achievement = achievement)
    suspend fun delete(achievement: Achievement) = achievementDao.delete(achievement = achievement)
}
package nz.ac.uclive.ojc31.seng440assignment2.data.achievements

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nz.ac.uclive.ojc31.seng440assignment2.model.Achievement

@Dao
interface AchievementDao {
    @Query("select * from achievement")
    fun getAllAchievements(): Flow<List<Achievement>>

    @Insert
    suspend fun insert(achievement: Achievement)

    @Update
    suspend fun update(achievement: Achievement)

    @Delete
    suspend fun delete(achievement: Achievement)
}
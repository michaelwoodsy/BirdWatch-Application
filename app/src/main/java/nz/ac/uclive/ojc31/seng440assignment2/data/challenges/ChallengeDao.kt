package nz.ac.uclive.ojc31.seng440assignment2.data.challenges

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nz.ac.uclive.ojc31.seng440assignment2.model.Challenge

@Dao
interface ChallengeDao {
    @Query("select * from challenge")
    fun getAllChallenges(): Flow<List<Challenge>>

    @Insert
    suspend fun insert(challenge: Challenge)

    @Update
    suspend fun update(challenge: Challenge)

    @Delete
    suspend fun delete(challenge: Challenge)

    @Query("DELETE FROM challenge WHERE challengeId = :challengeId")
    suspend fun deleteById(challengeId: Long)
}
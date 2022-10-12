package nz.ac.uclive.ojc31.seng440assignment2.repository

import kotlinx.coroutines.flow.Flow
import nz.ac.uclive.ojc31.seng440assignment2.data.challenges.ChallengeDao
import nz.ac.uclive.ojc31.seng440assignment2.model.Challenge

class ChallengeRepository (
    private val challengeDao: ChallengeDao
) {
    fun getAllFlow(): Flow<List<Challenge>> = challengeDao.getAllEntries()
    suspend fun insert(challenge: Challenge) = challengeDao.insert(challenge = challenge)
    suspend fun update(challenge: Challenge) = challengeDao.update(challenge = challenge)
    suspend fun delete(challenge: Challenge) = challengeDao.delete(challenge = challenge)
}
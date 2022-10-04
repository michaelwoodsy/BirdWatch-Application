package nz.ac.uclive.ojc31.seng440assignment2.repository

import kotlinx.coroutines.flow.Flow
import nz.ac.uclive.ojc31.seng440assignment2.data.EntryDao
import nz.ac.uclive.ojc31.seng440assignment2.model.Entry

class EntryRepository(
    private val entryDao: EntryDao
) {

    fun getAllFlow(): Flow<List<Entry>> = entryDao.getAllEntries()
    suspend fun insert(entry: Entry) = entryDao.insert(entry = entry)
    suspend fun update(entry: Entry) = entryDao.update(entry = entry)
    suspend fun delete(entry: Entry) = entryDao.delete(entry = entry)
}
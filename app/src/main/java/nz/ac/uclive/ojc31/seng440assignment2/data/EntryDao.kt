package nz.ac.uclive.ojc31.seng440assignment2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nz.ac.uclive.ojc31.seng440assignment2.model.Entry

@Dao
interface EntryDao {

    @Query("select * from entry")
    fun getAllEntries(): Flow<List<Entry>>

    @Insert
    suspend fun insert(entry: Entry)

    @Update
    suspend fun update(entry: Entry)

    @Delete
    suspend fun delete(entry: Entry)
}
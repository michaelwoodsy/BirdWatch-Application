package nz.ac.uclive.ojc31.seng440assignment2.data.entries

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nz.ac.uclive.ojc31.seng440assignment2.model.Entry
import java.time.LocalDate

@Dao
interface EntryDao {

    @Query("select * from entry")
    fun getAllEntries(): Flow<List<Entry>>

    @Query("select count(*) from entry")
    fun getEntryCount(): Flow<List<Int>>

    @Query("select observedDate from entry order by observedDate desc limit 1")
    fun getMostRecent(): Flow<List<LocalDate>>

    @Query("select observedDate from entry order by observedDate asc limit 1")
    fun getFirst(): Flow<List<LocalDate>>

    @Query("select speciesCode from entry group by speciesCode order by count(*) desc limit 1")
    fun getMostCommon(): Flow<List<String>>

    @Insert
    suspend fun insert(entry: Entry)

    @Update
    suspend fun update(entry: Entry)

    @Delete
    suspend fun delete(entry: Entry)
}
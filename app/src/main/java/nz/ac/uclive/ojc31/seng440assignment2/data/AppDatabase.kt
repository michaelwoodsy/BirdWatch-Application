package nz.ac.uclive.ojc31.seng440assignment2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import nz.ac.uclive.ojc31.seng440assignment2.data.challenges.ChallengeDao
import nz.ac.uclive.ojc31.seng440assignment2.data.entries.EntryDao
import nz.ac.uclive.ojc31.seng440assignment2.model.Challenge
import nz.ac.uclive.ojc31.seng440assignment2.model.Entry

@Database(entities = [Entry::class, Challenge::class], version = 6)
abstract class AppDatabase : RoomDatabase() {

    abstract fun entryDao(): EntryDao
    abstract fun challengeDao(): ChallengeDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "entry.db")
                    // add migrations here
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as AppDatabase
        }
    }
}
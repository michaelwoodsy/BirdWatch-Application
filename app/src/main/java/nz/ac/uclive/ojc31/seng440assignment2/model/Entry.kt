package nz.ac.uclive.ojc31.seng440assignment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entry")
data class Entry(
    @PrimaryKey(autoGenerate = true) var entryId: Long? = null,
    var text: String,
)
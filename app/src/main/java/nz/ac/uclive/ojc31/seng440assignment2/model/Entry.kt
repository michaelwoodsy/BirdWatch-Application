package nz.ac.uclive.ojc31.seng440assignment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.nio.DoubleBuffer

@Entity(tableName = "entry")
data class Entry(
    @PrimaryKey(autoGenerate = true) var entryId: Long? = null,
    var speciesCode: String,
    var observedDate: String,
    var observedLocation: String,
    var observedLat: Double,
    var observedLong: Double,
)
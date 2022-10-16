package nz.ac.uclive.ojc31.seng440assignment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import nz.ac.uclive.ojc31.seng440assignment2.data.converters.LocalDateConverter
import java.time.LocalDate

@Entity(tableName = "entry")
data class Entry(
    @PrimaryKey(autoGenerate = true) var entryId: Long? = null,
    var speciesCode: String,
    var observedDate: LocalDate,
    var observedLocation: String,
    var observedLat: Double,
    var observedLong: Double,
    var imageId: Int?,
)
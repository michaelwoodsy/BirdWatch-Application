package nz.ac.uclive.ojc31.seng440assignment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenge")
data class Challenge(
    @PrimaryKey(autoGenerate = true) var challengeId: Long? = null,
    var speciesCode: String,
    var receivedDate: String,
    var lastSeenLat: Double,
    var lastSeenLong: Double
)
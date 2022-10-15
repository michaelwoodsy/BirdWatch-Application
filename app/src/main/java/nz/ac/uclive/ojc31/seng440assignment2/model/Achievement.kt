package nz.ac.uclive.ojc31.seng440assignment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievement")
data class Achievement (
    @PrimaryKey(autoGenerate = true) var achievementId: Long? = null,
    var achievementType: String,
    var receivedDate: String,
    var achievementText: String,
)
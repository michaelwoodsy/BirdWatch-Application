package nz.ac.uclive.ojc31.seng440assignment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "achievement")
data class Achievement (
    @PrimaryKey(autoGenerate = true) var achievementId: Long? = null,
    var achievementCount: Int,
    var achievementType: String,
    var receivedDate: LocalDate,
    var achievementText: String,
)
package nz.ac.uclive.ojc31.seng440assignment2.data.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun StringToLocalDate(string: String?): LocalDate? {
        return if (string == null) null else LocalDate.parse(string)
    }

    @TypeConverter
    fun LocalDateToString(date: LocalDate?): String? {
        return date?.toString()
    }
}

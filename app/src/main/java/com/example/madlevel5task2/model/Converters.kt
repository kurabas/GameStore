package com.example.madlevel5task2.model

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromLongToTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun fromTimestampToDate(date: Date?): Long? = date?.time
}

package com.android.assignment.local.converter

import androidx.room.TypeConverter
import com.android.assignment.model.SpokenLanguage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SpokenLanguageListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<SpokenLanguage>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(jsonString: String?): List<SpokenLanguage>? {
        if (jsonString == null) {
            return null
        }
        val type = object : TypeToken<List<SpokenLanguage>>() {}.type
        return gson.fromJson(jsonString, type)
    }
}
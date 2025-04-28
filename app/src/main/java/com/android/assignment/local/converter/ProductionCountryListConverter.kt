package com.android.assignment.local.converter

import androidx.room.TypeConverter
import com.android.assignment.model.ProductionCountry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductionCountryListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<ProductionCountry>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(jsonString: String?): List<ProductionCountry>? {
        if (jsonString == null) {
            return null
        }
        val type = object : TypeToken<List<ProductionCountry>>() {}.type
        return gson.fromJson(jsonString, type)
    }
}
package com.android.assignment.local.converter

import androidx.room.TypeConverter
import com.android.assignment.model.ProductionCompany
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductionCompanyListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<ProductionCompany>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(jsonString: String?): List<ProductionCompany>? {
        if (jsonString == null) {
            return null
        }
        val type = object : TypeToken<List<ProductionCompany>>() {}.type
        return gson.fromJson(jsonString, type)
    }
}
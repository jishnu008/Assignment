package com.android.assignment.local.converter



import androidx.room.TypeConverter
import com.android.assignment.model.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GenreListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromGenreList(genres: List<Genre>?): String? {
        return gson.toJson(genres)
    }

    @TypeConverter
    fun toGenreList(genreString: String?): List<Genre>? {
        if (genreString == null) {
            return null
        }
        val listType = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(genreString, listType)
    }
}
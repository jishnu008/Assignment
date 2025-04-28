
package com.android.assignment.model

import com.google.gson.annotations.SerializedName


data class MovieDetailResponse(
    val adult: Boolean?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val budget: Int?,
    val genres: List<Genre>?,
    val homepage: String?,
    val id: Int?,
    val imdb_id: String?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?, // <--- Look for this property
    val popularity: Double?,
    @SerializedName("poster_path")
    val posterPath: String?,
    val production_companies: List<ProductionCompany>?,
    val production_countries: List<ProductionCountry>?,
    @SerializedName("release_date")
    val release_date: String?,
    val revenue: Int?,
    val runtime: Int?,
    val spoken_languages: List<SpokenLanguage>?,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val video: Boolean?,
    @SerializedName("vote_average")
    val vote_average: Double?,
    val vote_count: Int?
)

data class Genre(
    val id: Int?,
    val name: String?
)

data class ProductionCompany(
    val id: Int?,
    @SerializedName("logo_path")
    val logoPath: String?,
    val name: String?,
    @SerializedName("origin_country")
    val originCountry: String?
)

data class ProductionCountry(
    @SerializedName("iso_3166_1")
    val iso31661: String?,
    val name: String?
)

data class SpokenLanguage(
    @SerializedName("english_name")
    val englishName: String?,
    @SerializedName("iso_639_1")
    val iso6391: String?,
    val name: String?
)
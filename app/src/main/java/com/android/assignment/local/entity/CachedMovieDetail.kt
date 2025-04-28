package com.android.assignment.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.android.assignment.local.converter.GenreListConverter
import com.android.assignment.local.converter.ProductionCompanyListConverter
import com.android.assignment.local.converter.ProductionCountryListConverter
import com.android.assignment.local.converter.SpokenLanguageListConverter
import com.android.assignment.model.Genre
import com.android.assignment.model.ProductionCompany
import com.android.assignment.model.ProductionCountry
import com.android.assignment.model.SpokenLanguage

@Entity(tableName = "movie_details")
@TypeConverters(
    GenreListConverter::class,
    ProductionCompanyListConverter::class,
    ProductionCountryListConverter::class,
    SpokenLanguageListConverter::class
)
data class CachedMovieDetail(
    @PrimaryKey val id: Int,
    val adult: Boolean? = null,
    val backdropPath: String? = null,
    val budget: Int? = null,
    val genres: List<Genre>? = null,
    val homepage: String? = null,
    val imdbId: String? = null,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val posterPath: String? = null,
    val productionCompanies: List<ProductionCompany>? = null,
    val productionCountries: List<ProductionCountry>? = null,
    val releaseDate: String? = null,
    val revenue: Int? = null,
    val runtime: Int? = null,
    val spokenLanguages: List<SpokenLanguage>? = null,
    val status: String? = null,
    val tagline: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null,
    val timestamp: Long
) {
    companion object {
        fun fromMovieDetailResponse(response: com.android.assignment.model.MovieDetailResponse): CachedMovieDetail {
            return CachedMovieDetail(
                adult = response.adult,
                backdropPath = response.backdropPath,
                budget = response.budget,
                genres = response.genres,
                homepage = response.homepage,
                id = response.id ?: 0,
                imdbId = response.imdb_id,
                originalLanguage = response.original_language,
                originalTitle = response.original_title,
                overview = response.overview,
                popularity = response.popularity,
                posterPath = response.posterPath,
                productionCompanies = response.production_companies,
                productionCountries = response.production_countries,
                releaseDate = response.release_date,
                revenue = response.revenue,
                runtime = response.runtime,
                spokenLanguages = response.spoken_languages,
                status = response.status,
                tagline = response.tagline,
                title = response.title,
                video = response.video,
                voteAverage = response.vote_average,
                voteCount = response.vote_count,
                timestamp = System.currentTimeMillis()
            )
        }

        fun CachedMovieDetail.toMovieDetailResponse(): com.android.assignment.model.MovieDetailResponse {
            return com.android.assignment.model.MovieDetailResponse(
                adult = adult,
                backdropPath = backdropPath,
                budget = budget,
                genres = genres,
                homepage = homepage,
                id = id,
                imdb_id = imdbId,
                original_language = originalLanguage,
                original_title = originalTitle,
                overview = overview,
                popularity = popularity,
                posterPath = posterPath,
                production_companies = productionCompanies,
                production_countries = productionCountries,
                release_date = releaseDate,
                revenue = revenue,
                runtime = runtime,
                spoken_languages = spokenLanguages,
                status = status,
                tagline = tagline,
                title = title,
                video = video,
                vote_average = voteAverage,
                vote_count = voteCount
            )
        }
    }
}
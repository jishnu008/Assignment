

package com.android.assignment.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.assignment.local.Dao.MovieDao
import com.android.assignment.local.Dao.MovieDetailDao
import com.android.assignment.local.Dao.OfflineUserDao
import com.android.assignment.local.Dao.UserDao
import com.android.assignment.local.entity.CachedMovie
import com.android.assignment.local.entity.CachedMovieDetail
import com.android.assignment.model.User
import com.android.assignment.model.UserEntity

@Database(
    entities = [UserEntity::class, User::class, CachedMovie::class, CachedMovieDetail::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun offlineUserDao(): OfflineUserDao
    abstract fun movieDao(): MovieDao
    abstract fun movieDetailDao(): MovieDetailDao
}
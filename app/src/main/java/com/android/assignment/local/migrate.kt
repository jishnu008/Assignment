package com.android.assignment.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4){
    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL("ALTER TABLE user_entity ADD COLUMN isPendingSync INTEGER NOT NULL DEFAULT 0")


        database.execSQL("ALTER TABLE offline_users ADD COLUMN isPendingSync INTEGER NOT NULL DEFAULT 0")
    }
}
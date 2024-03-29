package com.example.ravely.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ravely.data.room.dao.MusicDao
import com.example.ravely.data.room.entities.MusicEntity

@Database(entities = [MusicEntity::class], version = 1)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
}
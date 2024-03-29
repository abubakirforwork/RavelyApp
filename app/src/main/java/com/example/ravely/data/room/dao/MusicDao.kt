package com.example.ravely.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.ravely.data.room.entities.MusicEntity

@Dao
interface MusicDao {

    @Query("SELECT * FROM music")
    fun getAllSongs(): List<MusicEntity>

    @Insert
    fun insert(music: MusicEntity)

    @Delete
    fun delete(music: MusicEntity)

}
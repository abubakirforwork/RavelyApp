package com.example.ravely.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("music")
data class MusicEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val musicFile: String
)
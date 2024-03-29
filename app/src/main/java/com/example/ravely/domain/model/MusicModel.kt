package com.example.ravely.domain.model

import java.io.Serializable

data class MusicModel(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val musicFile: String
) : Serializable
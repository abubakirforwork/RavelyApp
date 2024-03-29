package com.example.ravely.domain.repository

import com.example.ravely.domain.model.MusicModel

interface MusicRepository {

    fun getAllMusic(): List<MusicModel>

}
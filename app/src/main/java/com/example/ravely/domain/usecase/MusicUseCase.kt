package com.example.ravely.domain.usecase

import com.example.ravely.domain.model.MusicModel
import com.example.ravely.domain.repository.MusicRepository

class MusicUseCase(private val repository: MusicRepository) {

    fun getAllMusic(): List<MusicModel> {
        return repository.getAllMusic()
    }

}
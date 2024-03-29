package com.example.ravely.domain.usecase

import com.example.ravely.domain.repository.PlayerRepository

class PlayerUseCase(private val repository: PlayerRepository) {

    fun getMusicById(musicId: Long): String {
        return repository.getMusicById(musicId)
    }

}
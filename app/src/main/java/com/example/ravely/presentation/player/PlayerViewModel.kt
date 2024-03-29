package com.example.ravely.presentation.player

import androidx.lifecycle.ViewModel
import com.example.ravely.domain.usecase.PlayerUseCase

class PlayerViewModel(private val playerUseCase: PlayerUseCase) : ViewModel() {

    fun getMusicById(musicId: Long): String {
        return playerUseCase.getMusicById(musicId)
    }

}
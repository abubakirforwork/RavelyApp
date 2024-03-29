package com.example.ravely.presentation.music

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ravely.domain.model.MusicModel
import com.example.ravely.domain.usecase.MusicUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicViewModel(private val musicUseCase: MusicUseCase) : ViewModel() {

    private val _musicList = MutableLiveData<List<MusicModel>>()
    val musicList: LiveData<List<MusicModel>> get() = _musicList

    fun getAllMusic() {
        CoroutineScope(Dispatchers.IO).launch {
            val music = musicUseCase.getAllMusic()
            _musicList.postValue(music)
        }
    }
}
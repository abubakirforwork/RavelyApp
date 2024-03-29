package com.example.ravely.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ravely.domain.model.MusicModel

class SharedViewModel : ViewModel() {

    private val _musicList = MutableLiveData<List<MusicModel>>()
    val musicList: LiveData<List<MusicModel>> = _musicList

    private val _position = MutableLiveData<Long>()
    val position: LiveData<Long> = _position

    fun setMusicList(musicList: List<MusicModel>) {
        _musicList.value = musicList
    }

    fun setPosition(position: Long) {
        _position.value = position
    }
}
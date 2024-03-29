package com.example.ravely.domain.repository

interface PlayerRepository {

    fun getMusicById(musicId: Long): String

}
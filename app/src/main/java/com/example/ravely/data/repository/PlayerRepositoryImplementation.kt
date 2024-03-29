package com.example.ravely.data.repository

import android.content.ContentResolver
import android.provider.MediaStore
import com.example.ravely.domain.repository.PlayerRepository

class PlayerRepositoryImplementation(private val contentResolver: ContentResolver) :
    PlayerRepository {

    override fun getMusicById(musicId: Long): String {
        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val selection = "${MediaStore.Audio.Media._ID} = ?"
        val selectionArgs = arrayOf(musicId.toString())

        val contentResolver = contentResolver.query(
            musicUri, projection, selection, selectionArgs, null
        )

        var musicUriString = ""

        contentResolver?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                musicUriString = it.getString(columnIndex)
            }
        }

        return musicUriString
    }
}
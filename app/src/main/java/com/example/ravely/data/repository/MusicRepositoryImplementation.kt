package com.example.ravely.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import com.example.ravely.domain.model.MusicModel
import com.example.ravely.domain.repository.MusicRepository

class MusicRepositoryImplementation(private val contentResolver: ContentResolver) :
    MusicRepository {

    override fun getAllMusic(): List<MusicModel> {
        val musicList = mutableListOf<MusicModel>()

        val contentResolver = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null
        )

        contentResolver?.use { cursor ->
            val getId = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val getTitle = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val getArtist = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val getAlbum = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val getDuration = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val getFile = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(getId)
                val title = cursor.getString(getTitle)
                val artist = cursor.getString(getArtist)
                val albumId = cursor.getLong(getAlbum)
                val duration = cursor.getLong(getDuration)
                val imageUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"), albumId
                ).toString()
                val musicFile = cursor.getString(getFile)

                val music = MusicModel(id, title, artist, imageUri, duration, musicFile)
                musicList.add(music)
            }
        }

        return musicList
    }
}
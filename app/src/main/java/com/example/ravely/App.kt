package com.example.ravely

import android.app.Application
import androidx.room.Room
import com.example.ravely.data.room.database.MusicDatabase
import com.example.ravely.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        database =
            Room.databaseBuilder(applicationContext, MusicDatabase::class.java, "music_database")
                .build()

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

    companion object {
        lateinit var database: MusicDatabase
    }
}
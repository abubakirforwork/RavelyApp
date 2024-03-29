package com.example.ravely.di

import android.media.MediaPlayer
import com.example.ravely.data.repository.MusicRepositoryImplementation
import com.example.ravely.data.repository.PlayerRepositoryImplementation
import com.example.ravely.domain.repository.MusicRepository
import com.example.ravely.domain.repository.PlayerRepository
import com.example.ravely.domain.usecase.MusicUseCase
import com.example.ravely.domain.usecase.PlayerUseCase
import com.example.ravely.presentation.SharedViewModel
import com.example.ravely.presentation.music.MusicViewModel
import com.example.ravely.presentation.player.PlayerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single<MusicRepository> { MusicRepositoryImplementation(androidContext().contentResolver) }
    single<PlayerRepository> { PlayerRepositoryImplementation(androidContext().contentResolver) }
}

val mediaPlayerModule = module {
    factory { MediaPlayer() }
}

val useCaseModule = module {
    factory { MusicUseCase(get()) }
    factory { PlayerUseCase(get()) }
}

val viewModelModule = module {
    viewModel { MusicViewModel(get()) }
    viewModel { PlayerViewModel(get()) }
    viewModel { SharedViewModel() }
}

val appModule = listOf(
    repositoryModule, mediaPlayerModule, useCaseModule, viewModelModule,
)
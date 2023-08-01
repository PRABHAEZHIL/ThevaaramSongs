package com.kvep.thevaaramsongs.di

import com.kvep.thevaaramsongs.data.remote.MusicDatabase
import com.kvep.thevaaramsongs.repository.TrackRepository
import com.kvep.thevaaramsongs.repository.TrackRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideTrackRepository(trackRepository: TrackRepositoryImpl): TrackRepository {
        return trackRepository
    }

    @Provides
    @Singleton
    fun provideMusicDatabase()=MusicDatabase()
}
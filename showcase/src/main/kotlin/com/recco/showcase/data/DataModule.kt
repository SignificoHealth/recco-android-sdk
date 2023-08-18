package com.recco.showcase.data

import android.content.Context
import androidx.room.Room
import com.recco.showcase.data.room.PaletteDao
import com.recco.showcase.data.room.ShowcaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): ShowcaseDatabase = Room.databaseBuilder(
        context,
        ShowcaseDatabase::class.java,
        "showcase-db"
    ).build()

    @Provides
    @Singleton
    fun providePaletteDao(database: ShowcaseDatabase): PaletteDao = database.paletteDao()
}

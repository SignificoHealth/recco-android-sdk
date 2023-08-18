package com.recco.showcase.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.recco.showcase.data.entities.ShowcasePaletteEntity

@Database(entities = [ShowcasePaletteEntity::class], version = 1)
abstract class ShowcaseDatabase : RoomDatabase() {
    abstract fun paletteDao(): PaletteDao
}

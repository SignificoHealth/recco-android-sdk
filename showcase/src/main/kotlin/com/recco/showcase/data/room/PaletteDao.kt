package com.recco.showcase.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.recco.showcase.data.entities.ShowcasePaletteEntity

@Dao
interface PaletteDao {
    @Query("SELECT * FROM palette")
    suspend fun get(): List<ShowcasePaletteEntity>

    @Insert
    suspend fun add(paletteEntity: ShowcasePaletteEntity)

    @Update
    suspend fun update(paletteEntity: ShowcasePaletteEntity)
}

package com.recco.showcase.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.recco.showcase.data.entities.ShowcasePaletteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaletteDao {
    @Query("SELECT * FROM palette")
    fun getAll(): Flow<List<ShowcasePaletteEntity>>

    @Query("SELECT * FROM palette WHERE id=:id ")
    suspend fun get(id: Int): ShowcasePaletteEntity

    @Insert
    suspend fun add(paletteEntity: ShowcasePaletteEntity): Long

    @Update
    suspend fun update(paletteEntity: ShowcasePaletteEntity)

    @Delete
    suspend fun delete(paletteEntity: ShowcasePaletteEntity)
}

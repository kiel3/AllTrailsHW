package com.kyleengler.alltrailshw.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kyleengler.alltrailshw.entity.local.FavoriteRestaurantEntity

@Dao
interface FavoriteRestaurantDao {
    @Query("SELECT * FROM FavoriteRestaurantEntity")
    suspend fun getAll(): List<FavoriteRestaurantEntity>

    @Insert
    fun insert(favorite: FavoriteRestaurantEntity)

    @Delete
    fun delete(favorite: FavoriteRestaurantEntity)
}
package com.kyleengler.alltrailshw.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kyleengler.alltrailshw.entity.local.FavoriteRestaurantEntity

@Database(entities = arrayOf(FavoriteRestaurantEntity::class), version = 1)
abstract class FavoriteRestaurantDatabase : RoomDatabase() {
    abstract fun favoriteRestaurantDao(): FavoriteRestaurantDao
}
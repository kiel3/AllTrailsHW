package com.kyleengler.alltrailshw.di

import android.content.Context
import androidx.room.Room
import com.kyleengler.alltrailshw.database.FavoriteRestaurantDao
import com.kyleengler.alltrailshw.database.FavoriteRestaurantDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {
    @Provides
    @Singleton
    fun provideFavoritesDao(@ApplicationContext context: Context): FavoriteRestaurantDao {
        val db = Room.databaseBuilder(
            context,
            FavoriteRestaurantDatabase::class.java, "favorite-database"
        ).build()
        return db.favoriteRestaurantDao()
    }
}
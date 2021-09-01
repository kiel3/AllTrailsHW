package com.kyleengler.alltrailshw.entity.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class FavoriteRestaurantEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long = Date().time
)
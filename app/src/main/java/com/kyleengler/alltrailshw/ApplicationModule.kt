package com.kyleengler.alltrailshw

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.kyleengler.alltrailshw.places.PlacesApi
import com.kyleengler.alltrailshw.places.PlacesApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {
    @Provides
    @Singleton
    fun providePlacesApi(okHttpClient: OkHttpClient): PlacesApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(PlacesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttp(@ApplicationContext context: Context,
                      placesApiKeyInterceptor: PlacesApiKeyInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .addInterceptor(placesApiKeyInterceptor)
            .build()
    }
}
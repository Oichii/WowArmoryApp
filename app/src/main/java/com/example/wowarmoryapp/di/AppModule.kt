package com.example.wowarmoryapp.di

import android.R.attr
import com.example.wowarmoryapp.data.remote.WowApi
import com.example.wowarmoryapp.repository.MountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import android.R.attr.password
import com.example.wowarmoryapp.util.Constants
import com.example.wowarmoryapp.util.Constants.BASE_URL
import okhttp3.OkHttpClient


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideMountRepository(api: WowApi) = MountRepository(api)

//    var credentials: String = Constants.CLIENT_ID + ":" + Constants.CLIENT_SECRET
//
//    // create Base64 encodet string
//    val basic = "Basic " + credentials.encodeToByteArray().toString()

    private val client =  OkHttpClient.Builder()
        .addInterceptor(BasicAuthInterceptor( Constants.CLIENT_ID, Constants.CLIENT_SECRET))
        .build()

    @Singleton
    @Provides
    fun provideWowApi():WowApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WowApi::class.java)
    }

}
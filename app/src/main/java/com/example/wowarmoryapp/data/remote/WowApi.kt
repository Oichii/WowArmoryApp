package com.example.wowarmoryapp.data.remote

import com.example.wowarmoryapp.data.remote.responses.*
import retrofit2.http.*

interface WowApi {
//    @POST("/oauth/token")
//    suspend fun postUserAuth(
//        @Header("Authentication") ,
//        @Query("grant_type"): grantTpe
//    ): accessToken

    @GET("/data/wow/mount/index")
    suspend fun getMountsList(
        @Query ("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ):MountsList

    @GET("/data/wow/mount/{mountId}")
    suspend fun getMount(
        @Path("mountId") mountId: Int,
        @Query ("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ): MountX

    @GET("/data/wow/media/creature-display/{creatureDisplayId}")
    suspend fun getDisplay(
        @Path("creatureDisplayId") creatureDisplayId: Int,
        @Query ("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("access_token") accessToken: String
    ):MountDisplay
}
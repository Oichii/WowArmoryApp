package com.example.wowarmoryapp.repository

import androidx.compose.ui.graphics.Color
import com.example.wowarmoryapp.data.remote.WowApi
import com.example.wowarmoryapp.data.remote.responses.MountDisplay
import com.example.wowarmoryapp.data.remote.responses.MountX
import com.example.wowarmoryapp.data.remote.responses.MountsList
import com.example.wowarmoryapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

@ActivityScoped
class MountRepository @Inject constructor(
    private val api: WowApi
){

    suspend fun getMountList(namespace: String, locale: String, accessToken: String): Resource<MountsList> {
        val response = try {
            api.getMountsList(namespace, locale, accessToken)
        } catch(e: Exception) {
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }

    suspend fun getMount(mountId: Int, namespace: String, locale: String, accessToken: String): Resource<MountX> {
        val response = try{
            api.getMount(mountId, namespace, locale, accessToken)

        }catch (e: Exception){
            return Resource.Error(e.message ?: "Unidentified error")

        }
        return Resource.Success(response)
    }

    suspend fun getDisplay(creatureDisplayId: Int, namespace: String, locale: String, accessToken: String): Resource<MountDisplay> {
        val response = try{
            api.getDisplay(creatureDisplayId, namespace, locale, accessToken)

        }catch (e: Exception){
            return Resource.Error(e.message ?: "Unidentified error")

        }
        return Resource.Success(response)
    }

}
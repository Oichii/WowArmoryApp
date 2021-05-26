package com.example.wowarmoryapp.mountsList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.wowarmoryapp.BuildConfig
import com.example.wowarmoryapp.data.models.MountListEntry
import com.example.wowarmoryapp.data.remote.responses.Mount
import com.example.wowarmoryapp.data.remote.responses.MountsList
import com.example.wowarmoryapp.repository.MountRepository
import com.example.wowarmoryapp.util.Constants.LOCALE
import com.example.wowarmoryapp.util.Constants.NAMESPACE

import com.example.wowarmoryapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Integer.min
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MountListViewModel @Inject constructor(
    private val repository: MountRepository
) : ViewModel() {

    var currIdx = 0
    val pageSize = 20
    var mountList = mutableStateOf<List<MountListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    lateinit var allMountsList: List<Mount>

    init {
        Timber.i("initialization of mount list")
        getAllMounts()
    }

    fun loadMountList() {
        viewModelScope.launch {
            isLoading.value = true
            when (val allMounts = repository.getMountList(NAMESPACE, LOCALE, BuildConfig.TOKEN)) {
                is Resource.Success -> {
                    val mountEntries = allMounts.data!!.mounts.map { entry ->
                        Timber.i("entry $entry")

                        var url = ""
                        when (val mount =
                            repository.getMount(entry.id, NAMESPACE, LOCALE, BuildConfig.TOKEN)) {
                            is Resource.Success -> {
                                when (val image = repository.getDisplay(
                                    mount.data!!.creature_displays[0].id,
                                    NAMESPACE,
                                    LOCALE,
                                    BuildConfig.TOKEN
                                )) {
                                    is Resource.Success -> {
                                        url = image.data!!.assets[0].value

                                    }
                                    is Resource.Error -> {
                                        url = ""
                                    }
                                }

                            }
                            is Resource.Error -> {
                                url = ""
                            }
                        }
                        println("hello this is url: $url")
                        MountListEntry(
                            entry.name.capitalize(Locale.ROOT),
                            url,
                            entry.id
                        )
                    }
                    loadError.value = ""
                    isLoading.value = false
                    mountList.value += mountEntries
                }
                is Resource.Error -> {
                    loadError.value = allMounts.message!!
                    isLoading.value = false
                }
            }
        }
    }

    fun fillMountsList() {
        viewModelScope.launch {
            println(allMountsList)

            val end = minOf(currIdx+pageSize, allMountsList.size)
            endReached.value = end >= allMountsList.size

            println("current index: $currIdx, $end")
            val mountEntries = allMountsList.subList(currIdx, end).map { entry ->
                Timber.i("entry $entry")
                //TODO: get image url
                var url = ""
                when (val mount =
                    repository.getMount(entry.id, NAMESPACE, LOCALE, BuildConfig.TOKEN)) {
                    is Resource.Success -> {
                        when (val image = repository.getDisplay(
                            mount.data!!.creature_displays[0].id,
                            NAMESPACE,
                            LOCALE,
                            BuildConfig.TOKEN
                        )) {
                            is Resource.Success -> {
                                url = image.data!!.assets[0].value

                            }
                            is Resource.Error -> {
                                url = ""
                            }
                        }

                    }
                    is Resource.Error -> {
                        url = ""
                    }
                }
                println("hello this is url: $url")
                MountListEntry(
                    entry.name.capitalize(Locale.ROOT),
                    url,
                    entry.id
                )
            }
            currIdx += pageSize

            loadError.value = ""
            isLoading.value = false
            mountList.value += mountEntries

        }
    }
//
    private fun getAllMounts() {
        viewModelScope.launch {
            println("getting all mounts\n\n")
            isLoading.value = true
            when (val allMounts = repository.getMountList(NAMESPACE, LOCALE, BuildConfig.TOKEN)) {
                is Resource.Success -> {
                    allMountsList = allMounts.data!!.mounts
                    loadError.value = ""
                    isLoading.value = false
                    println("allmounts len: ${allMountsList.size}")
                    fillMountsList()
                }
                is Resource.Error -> {
                    loadError.value = allMounts.message!!
                    isLoading.value = false
                    allMountsList = listOf()
                }
            }
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate() { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }

        }
    }

}

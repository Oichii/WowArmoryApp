package com.example.wowarmoryapp.mountsList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.wowarmoryapp.data.models.MountListEntry
import com.example.wowarmoryapp.data.remote.responses.Mount
import com.example.wowarmoryapp.data.remote.responses.MountsList
import com.example.wowarmoryapp.repository.MountRepository
import com.example.wowarmoryapp.util.Constants.LOCALE
import com.example.wowarmoryapp.util.Constants.NAMESPACE
import com.example.wowarmoryapp.util.Constants.TOKEN
import com.example.wowarmoryapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
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

    init {
        loadMountList()
        println("mount list loaded")
    }


    fun loadMountList() {
        viewModelScope.launch {
            isLoading.value = true
            val allMounts = repository.getMountList(NAMESPACE, LOCALE, TOKEN)
            when (allMounts) {
                is Resource.Success -> {
                    val mountEntries = allMounts.data!!.mounts.map { entry ->
                        Timber.i("entry $entry")
                        MountListEntry(
                            entry.name.capitalize(Locale.ROOT),
                            "",
                            entry.id
                        )
                    }
                    loadError.value = ""
                    isLoading.value = false
                    mountList.value += mountEntries
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

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

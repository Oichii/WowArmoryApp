package com.example.wowarmoryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember

import androidx.compose.ui.graphics.Color

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.wowarmoryapp.mountsList.MountsListScreen
import com.example.wowarmoryapp.ui.theme.WowArmoryAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WowArmoryAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "mounts_list_screen"
                ){
                    composable("mounts_list_screen"){
                        MountsListScreen(navController = navController)
                    }
                    composable(
                        "mount_detail_screen/{dominantColor}/{mountName}",
                        arguments = listOf(
                            navArgument("dominantColor"){
                                type = NavType.IntType
                                this.defaultValue = Color.LightGray
                            },
                            navArgument("mountName"){
                                type = NavType.StringType
                            },

                        )
                    ){
                        val dominantColor = remember{
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) }?: Color.White
                        }
                        val mountName = remember{
                            it.arguments?.getString("mountName")
                        }

                    }
                }

            }
        }
    }
}


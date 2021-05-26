package com.example.wowarmoryapp.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.wowarmoryapp.R

val fontFamily = FontFamily(
    Font(R.font.open_sans_condensed_bold, weight = FontWeight.Bold),
    Font(R.font.open_sans_condensed_light, weight = FontWeight.Light),
    Font(R.font.open_sans_condensed_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
)
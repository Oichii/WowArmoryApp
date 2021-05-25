package com.example.wowarmoryapp.data.remote.responses

data class MountX(
    val _links: LinksX,
    val creature_displays: List<CreatureDisplay>,
    val description: String,
    val id: Int,
    val name: String,
    val should_exclude_if_uncollected: Boolean,
    val source: Source
)
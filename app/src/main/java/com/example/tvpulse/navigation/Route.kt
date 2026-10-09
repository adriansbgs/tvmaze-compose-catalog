package com.example.tvpulse.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MainRouteKey
@Serializable
data class DetailRouteKey(val id: Int)

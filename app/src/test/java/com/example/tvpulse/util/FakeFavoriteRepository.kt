package com.example.tvpulse.util

import com.example.tvpulse.domain.model.Show
import com.example.tvpulse.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/** In-memory stand-in for Room: every write re-emits, just like a Room Flow query. */
class FakeFavoriteRepository : FavoriteRepository {
    private val favorites = MutableStateFlow<List<Show>>(emptyList())

    override fun observeFavorites(): Flow<List<Show>> = favorites

    override fun observeIsFavorite(id: Int): Flow<Boolean> =
        favorites.map { list -> list.any { it.id == id } }

    override suspend fun toggle(show: Show) = favorites.update { list ->
        if (list.any { it.id == show.id }) list.filterNot { it.id == show.id } else list + show
    }

    override suspend fun remove(id: Int) = favorites.update { list -> list.filterNot { it.id == id } }
}

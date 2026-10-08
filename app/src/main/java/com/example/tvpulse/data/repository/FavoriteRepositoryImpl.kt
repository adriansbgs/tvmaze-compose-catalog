package com.example.tvpulse.data.repository

import com.example.tvpulse.data.local.FavoriteShowDao
import com.example.tvpulse.data.mapper.toDomain
import com.example.tvpulse.data.mapper.toEntity
import com.example.tvpulse.domain.model.Show
import com.example.tvpulse.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteShowDao,
) : FavoriteRepository {

    override fun observeFavorites(): Flow<List<Show>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeIsFavorite(id: Int): Flow<Boolean> = dao.observeIsFavorite(id)

    override suspend fun toggle(show: Show) {
        if (dao.observeIsFavorite(show.id).first()) dao.delete(show.id) else dao.insert(show.toEntity())
    }

    override suspend fun remove(id: Int) = dao.delete(id)
}

package com.example.tvpulse.data.mapper

import com.example.tvpulse.util.testShow
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteMapperTest {

    @Test
    fun `show survives a round trip through the entity`() {
        val show = testShow(1)

        assertEquals(show, show.toEntity(savedAt = 0L).toDomain())
    }

    @Test
    fun `empty genres stay empty after a round trip`() {
        val show = testShow(1).copy(genres = emptyList())

        assertEquals(emptyList<String>(), show.toEntity(savedAt = 0L).toDomain().genres)
    }
}

package com.example.tvpulse.data.mapper

import com.example.tvpulse.data.remote.dto.ImageDto
import com.example.tvpulse.data.remote.dto.RatingDto
import com.example.tvpulse.data.remote.dto.ShowDto
import com.example.tvpulse.domain.model.Show
import org.junit.Assert.assertEquals
import org.junit.Test

class ShowMapperTest {

    @Test
    fun `maps every field from a full dto`() {
        val dto = ShowDto(
            id = 169,
            name = "Breaking Bad",
            genres = listOf("Drama", "Crime"),
            status = "Ended",
            runtime = 60,
            rating = RatingDto(average = 9.2),
            image = ImageDto(medium = "m.jpg", original = "o.jpg"),
            summary = "<p><b>Breaking Bad</b></p>",
        )

        assertEquals(
            Show(
                id = 169,
                name = "Breaking Bad",
                imageMedium = "m.jpg",
                imageOriginal = "o.jpg",
                rating = 9.2,
                genres = listOf("Drama", "Crime"),
                runtime = 60,
                status = "Ended",
                summary = "<p><b>Breaking Bad</b></p>",
            ),
            dto.toDomain(),
        )
    }

    @Test
    fun `missing image, rating, runtime and summary map to null without crashing`() {
        val show = ShowDto(id = 1, name = "No Data", rating = RatingDto(average = null)).toDomain()

        assertEquals(null, show.imageMedium)
        assertEquals(null, show.imageOriginal)
        assertEquals(null, show.rating)
        assertEquals(null, show.runtime)
        assertEquals(null, show.summary)
        assertEquals(emptyList<String>(), show.genres)
    }
}

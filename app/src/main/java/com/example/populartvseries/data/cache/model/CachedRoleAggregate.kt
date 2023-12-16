package com.example.populartvseries.data.cache.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.populartvseries.domain.model.Role


data class CachedRoleAggregate(
    @Embedded
    val role: CachedRole,
    @Relation(parentColumn = "artist_id", entityColumn = "artist_id")
    val artist: CachedArtist
) {
    fun toDomain() =
        Role(role.artist_id, role.series_id, artist.name, artist.image, role.character)
}
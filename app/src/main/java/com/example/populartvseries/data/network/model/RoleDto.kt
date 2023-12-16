package com.example.populartvseries.data.network.model

import com.example.populartvseries.domain.model.Role
import com.google.gson.annotations.SerializedName

class RoleDto(
    @SerializedName("id") val artist_id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("profile_path") val image: String,
    @SerializedName("character") val character: String
){
    fun toDomain(seriesId:Int) = Role(artist_id, seriesId, name, image, character)
}

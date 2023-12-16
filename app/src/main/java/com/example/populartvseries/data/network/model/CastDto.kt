package com.example.populartvseries.data.network.model

import com.google.gson.annotations.SerializedName

class CastDto(
    @SerializedName("cast") val roleList: List<RoleDto>
)

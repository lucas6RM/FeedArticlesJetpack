package com.mercierlucas.feedarticlesjetpack.data.network.responses.dtos

import com.squareup.moshi.Json

data class ReturnLoginDto(
    @Json(name = "status")
    val status: Int,
    @Json(name = "id")
    val id: Long,
    @Json(name = "token")
    val token: String?
)
package com.mercierlucas.feedarticlesjetpack.data.network.responses.dtos


import com.squareup.moshi.Json

data class ReturnStatusDto(
    @Json(name = "status")
    val status: Int
)
package com.mercierlucas.feedarticlesjetpack.data.network.requests.dtos


import com.squareup.moshi.Json

data class UpdateArticleDto(
    @Json(name = "cat")
    val cat: Int,
    @Json(name = "desc")
    val desc: String,
    @Json(name = "id")
    val id: Long,
    @Json(name = "image")
    val image: String,
    @Json(name = "title")
    val title: String
)
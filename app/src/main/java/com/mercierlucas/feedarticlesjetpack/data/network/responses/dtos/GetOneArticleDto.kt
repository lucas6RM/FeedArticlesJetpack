package com.mercierlucas.feedarticlesjetpack.data.network.responses.dtos


import com.mercierlucas.feedarticlesjetpack.data.entity.dtos.Article
import com.squareup.moshi.Json

data class GetOneArticleDto(
    @Json(name = "article")
    val article: Article,
    @Json(name = "status")
    val status: String
)
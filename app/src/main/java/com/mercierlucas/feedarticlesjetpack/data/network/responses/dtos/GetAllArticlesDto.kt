package com.mercierlucas.feedarticlesjetpack.data.network.responses.dtos


import com.mercierlucas.feedarticlesjetpack.data.entity.dtos.Article
import com.squareup.moshi.Json

data class GetAllArticlesDto(
    @Json(name = "articles")
    val articles: List<Article>,
    @Json(name = "status")
    val status: String
)
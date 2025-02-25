package com.mercierlucas.feedarticlesjetpack.data.entity.dtos


import com.squareup.moshi.Json

data class Article(
    @Json(name = "categorie")
    val categorie: Int,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "descriptif")
    val descriptif: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "id_u")
    val idU: Int,
    @Json(name = "is_fav")
    val isFav: Int,
    @Json(name = "titre")
    val titre: String,
    @Json(name = "url_image")
    val urlImage: String
)
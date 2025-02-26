package com.mercierlucas.feedarticlesjetpack.data.network.repositories

import com.mercierlucas.feedarticlesjetpack.data.network.api.ApiService
import com.mercierlucas.feedarticlesjetpack.data.network.requests.dtos.CreaArticleDto
import com.mercierlucas.feedarticlesjetpack.data.network.requests.dtos.UpdateArticleDto
import javax.inject.Inject

class ArticleRepository @Inject constructor(private val apiService: ApiService){

    suspend fun getAllArticles(withFav: Int, token: String?)
        = apiService.getAllArticles(withFav, token)

    suspend fun getOneArticle(token: String?, id: Long, withFav: Int)
        = apiService.getOneArticle(token, id, withFav)

    suspend fun updateArticleEdited(id: Long, token: String?, updateArticle: UpdateArticleDto)
        = apiService.updateArticleEdited(id, token, updateArticle)

    suspend fun deleteArticleEdited(id: Long, token: String?)
        = apiService.deleteArticleEdited(id, token)

    suspend fun createNewArticle(token: String?, creaArticle: CreaArticleDto)
        = apiService.createNewArticle(token, creaArticle)

    suspend fun addArticleToFavorites(id : Long, token: String?)
        = apiService.addArticleToFavorites(id, token)

}
package com.mercierlucas.feedarticlesjetpack.data.network.api

import com.mercierlucas.feedarticlesjetpack.data.network.requests.dtos.CreaArticleDto
import com.mercierlucas.feedarticlesjetpack.data.network.requests.dtos.RegisterDto
import com.mercierlucas.feedarticlesjetpack.data.network.requests.dtos.UpdateArticleDto
import com.mercierlucas.feedarticlesjetpack.data.network.responses.dtos.GetAllArticlesDto
import com.mercierlucas.feedarticlesjetpack.data.network.responses.dtos.GetOneArticleDto
import com.mercierlucas.feedarticlesjetpack.data.network.responses.dtos.ReturnLoginDto
import com.mercierlucas.feedarticlesjetpack.data.network.responses.dtos.ReturnStatusDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @PUT(ApiRoutes.REGISTER)
    suspend fun registerUserAndGetToken(
        @Body registerDto: RegisterDto
    ) : Response<ReturnLoginDto>?

    @FormUrlEncoded
    @POST(ApiRoutes.LOGIN)
    suspend fun loginUserAndGetToken(
        @Field("login") userLogin : String,
        @Field("mdp") userPassword : String
    ) : Response<ReturnLoginDto>?

    @GET(ApiRoutes.GET_ALL_ARTICLES)
    suspend fun getAllArticles(
        @Query("with_fav") withFav : Int,
        @Header("token") token: String?
    ) : Response<GetAllArticlesDto>?

    @GET(ApiRoutes.GET_ONE_ARTICLE)
    suspend fun getOneArticle(
        @Header("token") token : String?,
        @Path("id") id : Long,
        @Query("with_fav") withFav : Int,
    ) : Response<GetOneArticleDto>?

    @POST(ApiRoutes.UPDATE_ARTICLE)
    suspend fun updateArticleEdited(
        @Path("id") id : Long,
        @Header("token") token : String?,
        @Body updateArticle : UpdateArticleDto
    ) : Response<ReturnStatusDto>?

    @DELETE(ApiRoutes.DELETE_ARTICLE)
    suspend fun deleteArticleEdited(
        @Path("id") id : Long,
        @Header("token") token : String?
    ) : Response<ReturnStatusDto>?

    @PUT(ApiRoutes.CREATE_ARTICLE)
    suspend fun createNewArticle(
        @Header("token") token : String?,
        @Body creaArticle: CreaArticleDto
    ) : Response<ReturnStatusDto>?

    @PUT(ApiRoutes.ADD_TO_FAVORITES)
    suspend fun addArticleToFavorites(
        @Path("id") id : Long,
        @Header("token") token : String?
    ) : Response<ReturnStatusDto>?

}
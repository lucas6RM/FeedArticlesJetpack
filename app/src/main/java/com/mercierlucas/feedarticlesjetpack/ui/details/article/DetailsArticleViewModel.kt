package com.mercierlucas.feedarticlesjetpack.ui.details.article

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercierlucas.feedarticlesjetpack.data.entity.dtos.Article
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefs
import com.mercierlucas.feedarticlesjetpack.data.network.repositories.ArticleRepository
import com.mercierlucas.feedarticlesjetpack.utils.WITH_FAV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsArticleViewModel @Inject constructor(
    private val myPrefs: MyPrefs,
    private val articleRepository: ArticleRepository
): ViewModel() {

    private val _articleToDisplay = MutableLiveData<Article>()
    val articleToDisplay : LiveData<Article> = _articleToDisplay

    private val _messageFromGetArticleResponse = MutableLiveData<String?>()
    val messageFromGetArticleResponse : LiveData<String?>
        get() = _messageFromGetArticleResponse

    private val _messageFromAddFavoriteResponse = MutableLiveData<String?>()
    val messageFromAddFavoriteResponse : LiveData<String?>
        get() = _messageFromAddFavoriteResponse


    fun getArticleById(articleId: Long) {
        viewModelScope.launch {
            val responseArticle = withContext(Dispatchers.IO){
                articleRepository.getOneArticle(
                    token = myPrefs.token,
                    id = articleId,
                    withFav = WITH_FAV)
            }
            val body = responseArticle?.body()
            when{
                responseArticle == null ->
                    Log.e(ContentValues.TAG,"Pas de reponse du serveur")

                responseArticle.isSuccessful && body != null -> {
                    _articleToDisplay.value = body.article
                    _messageFromGetArticleResponse.value = body.status
                }
                else -> responseArticle.errorBody()?.let { Log.e(ContentValues.TAG, it.string()) }
            }
        }
    }

    fun toggleAddToFavorites(id: Long) {
        viewModelScope.launch {
            val responseFavorite = withContext(Dispatchers.IO){
                articleRepository.addArticleToFavorites(id = id, token = myPrefs.token)
            }
            val body = responseFavorite?.body()
            when{
                responseFavorite == null ->
                    Log.e(ContentValues.TAG,"Pas de reponse du serveur")

                responseFavorite.isSuccessful && body != null ->
                    _messageFromAddFavoriteResponse.value = body.status.toString()

                else -> responseFavorite.errorBody()?.let { Log.e(ContentValues.TAG, it.string()) }
            }
        }
    }

}
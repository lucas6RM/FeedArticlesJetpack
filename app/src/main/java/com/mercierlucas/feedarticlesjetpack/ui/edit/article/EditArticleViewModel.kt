package com.mercierlucas.feedarticlesjetpack.ui.edit.article

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercierlucas.feedarticlesjetpack.data.entity.dtos.Article
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefs
import com.mercierlucas.feedarticlesjetpack.data.network.repositories.ArticleRepository
import com.mercierlucas.feedarticlesjetpack.data.network.requests.dtos.UpdateArticleDto
import com.mercierlucas.feedarticlesjetpack.utils.WITH_FAV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditArticleViewModel @Inject constructor(
    private val myPrefs: MyPrefs,
    private val articleRepository: ArticleRepository
): ViewModel() {
    private val _articleToEdit = MutableLiveData<Article>()
    val articleToEdit : LiveData<Article> = _articleToEdit

    private val _messageFromGetArticleResponse = MutableLiveData<String?>()
    val messageFromGetArticleResponse : LiveData<String?>
        get() = _messageFromGetArticleResponse

    private val _messageFromUpdateArticleResponse = MutableLiveData<String?>()
    val messageFromUpdateArticleResponse : LiveData<String?>
        get() = _messageFromUpdateArticleResponse

    private val _messageFromDeleteArticleResponse = MutableLiveData<String?>()
    val messageFromDeleteArticleResponse : LiveData<String?>
        get() = _messageFromDeleteArticleResponse

    private val _isResponseCorrect = MutableLiveData<Boolean>()
    val isResponseCorrect: LiveData<Boolean>
        get() = _isResponseCorrect

    init {
        _isResponseCorrect.value = false
    }


    fun getArticleById(articleId: Long) {
        viewModelScope.launch {
            val responseArticle = withContext(Dispatchers.IO){
                articleRepository.getOneArticle(
                    token = myPrefs.token,
                    id = articleId,
                    withFav = WITH_FAV
                )
            }
            val body = responseArticle?.body()
            when{
                responseArticle == null ->
                    Log.e(ContentValues.TAG,"Pas de reponse du serveur")

                responseArticle.isSuccessful && body != null -> {
                    _articleToEdit.value = body.article
                    _messageFromGetArticleResponse.value = body.status
                }
                else -> responseArticle.errorBody()?.let { Log.e(ContentValues.TAG, it.string()) }
            }
        }
    }

    fun updateArticle(
        articleId: Long,
        title: String,
        content: String,
        imageUrl: String,
        articleCategory: Int
    ){
        viewModelScope.launch {
            val responseUpdate = withContext(Dispatchers.IO){
                articleRepository.updateArticleEdited(
                    id = articleId,
                    token = myPrefs.token,
                    updateArticle = UpdateArticleDto(
                        cat = articleCategory,
                        desc = content,
                        id = articleId,
                        image = imageUrl,
                        title = title
                    )
                )
            }
            val body = responseUpdate?.body()
            when{
                responseUpdate == null ->
                    Log.e(ContentValues.TAG,"Pas de reponse du serveur")

                responseUpdate.isSuccessful && body != null -> {
                    _messageFromUpdateArticleResponse.value = body.status.toString()
                    _isResponseCorrect.value = true
                }
                else -> responseUpdate.errorBody()?.let { Log.e(ContentValues.TAG, it.string()) }
            }
        }
    }

    fun resetIsResponseCorrect() {
        _isResponseCorrect.value = false
    }

    fun deleteArticle(articleId: Long) {
        viewModelScope.launch {
            val responseDelete = withContext(Dispatchers.IO){
                articleRepository.deleteArticleEdited(id = articleId, token = myPrefs.token)
            }
            val body = responseDelete?.body()
            when{
                responseDelete == null ->
                    Log.e(ContentValues.TAG,"Pas de reponse du serveur")

                responseDelete.isSuccessful && body != null -> {
                    _messageFromDeleteArticleResponse.value = body.status.toString()
                    _isResponseCorrect.value = true
                }
                else -> responseDelete.errorBody()?.let { Log.e(ContentValues.TAG, it.string()) }
            }
        }

    }

}
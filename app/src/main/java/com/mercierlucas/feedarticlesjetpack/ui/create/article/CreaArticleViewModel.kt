package com.mercierlucas.feedarticlesjetpack.ui.create.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefs
import com.mercierlucas.feedarticlesjetpack.data.network.repositories.ArticleRepository
import com.mercierlucas.feedarticlesjetpack.data.network.requests.dtos.CreaArticleDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class CreaArticleViewModel @Inject constructor(
    private val myPrefs: MyPrefs,
    private val articleRepository: ArticleRepository
): ViewModel() {

    private val _isResponseCorrect = MutableLiveData<Boolean>()
    val isResponseCorrect: LiveData<Boolean>
        get() = _isResponseCorrect

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage : LiveData<String?>
        get() = _errorMessage

    init {
        _isResponseCorrect.value = false
    }

    fun addNewArticle(title: String, content: String, imageUrl: String, articleCategory: Int) {
        viewModelScope.launch {
            val responseAddNewArticle = withContext(Dispatchers.IO){
                articleRepository
                    .createNewArticle(
                        myPrefs.token,
                        CreaArticleDto(
                            cat = articleCategory,
                            desc = content,
                            idU = myPrefs.userId,
                            image = imageUrl,
                            title = title
                    )
                )
            }
            val body = responseAddNewArticle?.body()
            when{
                responseAddNewArticle == null -> _errorMessage.value = "Pas de reponse Serveur"
                responseAddNewArticle.isSuccessful && body != null -> {
                    _isResponseCorrect.value = true
                }
                else -> {
                    _errorMessage.value = body?.status.toString()
                }
            }
        }
    }

    fun resetIsResponseCorrect() {
        _isResponseCorrect.value = false
    }

}
package com.mercierlucas.feedarticlesjetpack.ui.main.articles

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
class MainViewModel @Inject constructor(
    private val myPrefs: MyPrefs,
    private val articleRepository: ArticleRepository) : ViewModel() {

    private val _articleList : MutableLiveData<List<Article>> = MutableLiveData(emptyList())
    val articleList : LiveData<List<Article>> = _articleList

    private val _articleFilteredList : MutableLiveData<List<Article>> = MutableLiveData(emptyList())
    val articleFilteredList : LiveData<List<Article>> = _articleFilteredList

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage : LiveData<String?>
        get() = _errorMessage


    init {
        refreshArticles()
        _articleFilteredList.value = _articleList.value
    }

    fun refreshArticles(){
        viewModelScope.launch {
            val responseGetAllArticles = withContext(Dispatchers.IO){
                articleRepository.getAllArticles(WITH_FAV,myPrefs.token)
            }
            val body = responseGetAllArticles?.body()
            when{
                responseGetAllArticles == null -> _errorMessage.value = "Pas de reponse Serveur"
                responseGetAllArticles.isSuccessful && body != null -> {
                    _articleList.value = body.articles.toList()
                }
                else -> {
                    _errorMessage.value = body?.status.toString()
                }
            }
        }
    }


    fun clearMySharedPreferences() {
        myPrefs.apply {
            userId = 0L
            token = null
        }
    }

    fun refreshfilteredArticles(categoryToFilter: Int) {

            resetfilteredArticles()

            val filteredList : MutableList<Article> = mutableListOf()
            for(article in _articleList.value!!){
                if(article.categorie == categoryToFilter)
                    filteredList.add(article)
            }
            _articleFilteredList.value = filteredList.toList()
    }

    fun resetfilteredArticles() {
        _articleFilteredList.value = _articleList.value
    }

}

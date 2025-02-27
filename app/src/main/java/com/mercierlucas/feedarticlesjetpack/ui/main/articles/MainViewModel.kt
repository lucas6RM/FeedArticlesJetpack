package com.mercierlucas.feedarticlesjetpack.ui.main.articles

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercierlucas.feedarticlesjetpack.data.entity.dtos.Article
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefs
import com.mercierlucas.feedarticlesjetpack.data.network.repositories.ArticleRepository
import com.mercierlucas.feedarticlesjetpack.utils.CATEGORY_TOUS
import com.mercierlucas.feedarticlesjetpack.utils.WITH_FAV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


enum class Destination{
    FRAG_DETAILS_ARTICLE, FRAG_EDIT_ARTICLE
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val myPrefs: MyPrefs,
    private val articleRepository: ArticleRepository) : ViewModel() {

    private val _articleList : MutableLiveData<List<Article>> = MutableLiveData(emptyList())
    val articleList : LiveData<List<Article>> = _articleList

    private val _articleFilteredList : MutableLiveData<List<Article>> = MutableLiveData(emptyList())
    val articleFilteredList : LiveData<List<Article>> = _articleFilteredList

    private val _isFilterFavActivated = MutableLiveData<Boolean>()
    val isFilterFavActivated: LiveData<Boolean>
        get() = _isFilterFavActivated

    private val _currentCategory = MutableLiveData<Int>()
    val currentCategory: LiveData<Int>
        get() = _currentCategory

    private val _articleIdClicked = MutableLiveData<Long>()
    val articleIdClicked : LiveData<Long> = _articleIdClicked

    private val _currentFragment = MutableLiveData<Destination?>()
    val currentFragment : LiveData<Destination?>
        get() = _currentFragment

    private val _messageFromGetAllPatientsResponse = MutableLiveData<String?>()
    val messageFromGetAllPatientsResponse : LiveData<String?>
        get() = _messageFromGetAllPatientsResponse


    init {
        _articleFilteredList.value = _articleList.value
        _articleIdClicked.value = 0L
        _currentFragment.value = null
        _isFilterFavActivated.value = false
        _currentCategory.value = CATEGORY_TOUS
    }

    fun refreshArticles(){
        viewModelScope.launch {
            val responseGetAllArticles = withContext(Dispatchers.IO){
                articleRepository.getAllArticles(WITH_FAV,myPrefs.token)
            }
            val body = responseGetAllArticles?.body()
            when{
                responseGetAllArticles == null ->
                    Log.e(ContentValues.TAG,"Pas de reponse du serveur")
                responseGetAllArticles.isSuccessful && body != null -> {
                    _articleList.value = body.articles.toList()
                    _messageFromGetAllPatientsResponse.value = body.status
                }
                else -> responseGetAllArticles.errorBody()?.let { Log.e(ContentValues.TAG, it.string()) }
            }
        }
    }

    fun clickOnAnArticleIsDone(articleId: Long, userId: Long) {
        if (userId == myPrefs.userId)
            goToEditArticle()
        else
            goToDetailsArticle()
        _articleIdClicked.value = articleId
    }

    private fun goToEditArticle() {
        _currentFragment.value = Destination.FRAG_EDIT_ARTICLE
    }

    private fun goToDetailsArticle() {
        _currentFragment.value = Destination.FRAG_DETAILS_ARTICLE
    }

    fun clearMySharedPreferences() {
        myPrefs.apply {
            userId = 0L
            token = null
        }
    }

    fun resetFilteredArticles() {
        _articleFilteredList.value = _articleList.value
    }

    fun resetCurrentFragment(){
        _currentFragment.value = null
    }

    fun setFilterByFavActivated(toggle: Boolean) {
        _isFilterFavActivated.value = toggle
    }

    fun setCurrentCategory(categoryChanged: Int) {
        _currentCategory.value = categoryChanged
    }

    fun refreshFilters() {
        val buttonFavOn = isFilterFavActivated.value
        val categoryToFilter = currentCategory.value
        val filteredList : MutableList<Article> = mutableListOf()

        resetFilteredArticles()
        if (categoryToFilter != CATEGORY_TOUS) {
            for (article in _articleList.value!!) {
                if (article.categorie == categoryToFilter)
                    filteredList.add(article)
            }
        }
        else
            articleList.value?.let { filteredList.addAll(it) }

        if (buttonFavOn == true ){
            val favFilteredList : MutableList<Article> = mutableListOf()
            for (article in filteredList){
                if(article.isFav == 1)
                    favFilteredList.add(article)
            }
            _articleFilteredList.value = favFilteredList.toList()
        }
        else
            _articleFilteredList.value = filteredList.toList()
    }


}

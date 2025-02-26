package com.mercierlucas.feedarticlesjetpack.ui.login

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefs
import com.mercierlucas.feedarticlesjetpack.data.network.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val myPrefs: MyPrefs,
    private val authRepository: AuthRepository) : ViewModel() {

    private val _isResponseCorrect = MutableLiveData<Boolean>()
    val isResponseCorrect: LiveData<Boolean>
        get() = _isResponseCorrect

    private val _messageFromLoginResponse = MutableLiveData<String?>()
    val messageFromLoginResponse : LiveData<String?>
        get() = _messageFromLoginResponse

    init {
        _isResponseCorrect.value = false
    }

    fun signIn(login: String, mdp : String){
        viewModelScope.launch {
            val responseLogin = withContext(Dispatchers.IO) {
                authRepository.loginUserAndGetToken(login, mdp)
            }
            val body = responseLogin?.body()
            when{
                responseLogin == null -> Log.e(ContentValues.TAG,"Pas de reponse du serveur")
                responseLogin.isSuccessful && body != null ->{
                    myPrefs.apply {
                        token = body.token
                        userId = body.id
                    }
                    _isResponseCorrect.value = true
                    _messageFromLoginResponse.value = body.status.toString()
                }
                else -> responseLogin.errorBody()?.let { Log.e(ContentValues.TAG, it.string()) }
            }
        }
    }

    fun resetIsResponseCorrect() {
        _isResponseCorrect.value = false
    }


}
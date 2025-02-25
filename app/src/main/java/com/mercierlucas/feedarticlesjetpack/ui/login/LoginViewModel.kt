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
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val myPrefs: MyPrefs,
    private val authRepository: AuthRepository) : ViewModel() {

    private val _isResponseCorrect = MutableLiveData<Boolean>()
    val isResponseCorrect: LiveData<Boolean>
        get() = _isResponseCorrect

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage : LiveData<String?>
        get() = _errorMessage

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
                responseLogin == null -> _errorMessage.value = "Pas de réponse Serveur"
                responseLogin.isSuccessful && body != null ->{
                    myPrefs.apply {
                        token = body.token
                        userId = body.id
                    }
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
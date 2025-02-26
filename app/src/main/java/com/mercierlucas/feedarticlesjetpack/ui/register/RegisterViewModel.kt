package com.mercierlucas.feedarticlesjetpack.ui.register

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefs
import com.mercierlucas.feedarticlesjetpack.data.network.repositories.AuthRepository
import com.mercierlucas.feedarticlesjetpack.data.network.requests.dtos.RegisterDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val myPrefs: MyPrefs,
    private val authRepository: AuthRepository): ViewModel() {

    private val _isResponseCorrect = MutableLiveData<Boolean>()
    val isResponseCorrect: LiveData<Boolean>
        get() = _isResponseCorrect

    private val _messageFromRegisterResponse = MutableLiveData<String?>()
    val messageFromRegisterResponse : LiveData<String?>
        get() = _messageFromRegisterResponse

    init {
        _isResponseCorrect.value = false
    }

    fun registerIn(registerDto: RegisterDto){
        viewModelScope.launch {
            val responseRegister = withContext(Dispatchers.IO) {
                authRepository.registerUserAndGetToken(registerDto)
            }
            val body = responseRegister?.body()
            when{
                responseRegister == null -> Log.e(ContentValues.TAG,"Pas de reponse du serveur")
                responseRegister.isSuccessful && body != null ->{
                    myPrefs.apply {
                        token = body.token
                        userId = body.id
                    }
                    _isResponseCorrect.value = true
                    _messageFromRegisterResponse.value = body.status.toString()
                }
                else -> responseRegister.errorBody()?.let { Log.e(ContentValues.TAG, it.string()) }
            }
        }
    }


    fun resetIsResponseCorrect() {
        _isResponseCorrect.value = false
    }
}
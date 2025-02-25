package com.mercierlucas.feedarticlesjetpack.ui.register

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

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage : LiveData<String?>
        get() = _errorMessage

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
                responseRegister == null -> _errorMessage.value = "Pas de réponse Serveur"
                responseRegister.isSuccessful && body != null ->{
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
package com.mercierlucas.feedarticlesjetpack.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val myPrefs: MyPrefs) : ViewModel() {

    private val _token = MutableLiveData<String?>()
    val token : LiveData<String?>
        get() = _token

    init {
        _token.value = myPrefs.token
    }
}
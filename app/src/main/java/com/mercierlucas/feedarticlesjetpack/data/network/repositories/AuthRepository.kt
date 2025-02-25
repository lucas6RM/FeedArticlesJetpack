package com.mercierlucas.feedarticlesjetpack.data.network.repositories

import com.mercierlucas.feedarticlesjetpack.data.network.api.ApiService
import com.mercierlucas.feedarticlesjetpack.data.network.requests.dtos.RegisterDto
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService){

    suspend fun registerUserAndGetToken(registerDto: RegisterDto)
        = apiService.registerUserAndGetToken(registerDto)

    suspend fun loginUserAndGetToken(login : String,mdp :String)
        = apiService.loginUserAndGetToken(login,mdp)

}
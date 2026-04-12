package ru.modas.cli.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.modas.cli.data.LoginRequest
import ru.modas.cli.data.LoginResponse

interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

}
package com.br.queroajudar.network

import com.br.queroajudar.model.Category
import com.br.queroajudar.model.Organization
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.model.formdata.LoginData
import com.br.queroajudar.model.formdata.RegisterData
import retrofit2.http.*

interface ApiService {

    companion object {
        const val BASE_URL = "http://192.168.1.109:8000/api/"
    }

    @GET("causes")
    suspend fun getCauses(): SuccessResponse<List<Category>>

    @GET("skills")
    suspend fun getSkills(): SuccessResponse<List<Category>>

    @POST("login")
    suspend fun postLogin(@Body data: LoginData): SuccessResponse<String>

    @POST("register")
    suspend fun postRegister(@Body data: RegisterData): SuccessResponse<String>

    @GET("organizations")
    suspend fun getOrganizations(
        @Query("page") page : Int
    ) : SuccessResponse<List<Organization>>


    @GET("vacancies")
    suspend fun getVacancies(
        @Query("page") page : Int?,
        @Query("causes_id") causes: String?,
        @Query("skills_id") skills : String?
    ): SuccessResponse<List<Vacancy>>
}
package com.br.queroajudar.network

import com.br.queroajudar.data.Category
import com.br.queroajudar.data.Organization
import com.br.queroajudar.data.User
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.data.formdata.LoginData
import com.br.queroajudar.data.formdata.RegisterData
import com.br.queroajudar.data.formdata.VacancyApplicationData
import retrofit2.http.*

interface ApiService {

    companion object {
        const val BASE_URL = "http://192.168.1.109:8000/api/"
        //const val BASE_URL = "https://quero-ajudar-292717.rj.r.appspot.com/api/"
    }

    @GET("causes")
    suspend fun getCauses(): SuccessResponse<List<Category>>

    @GET("skills")
    suspend fun getSkills(): SuccessResponse<List<Category>>

    @POST("causes/update-user-causes")
    suspend fun postUpdateUserCauses(
        @Query("causes_ids[]") causes_ids: List<Int>
    ): SuccessResponse<Boolean>

    @POST("skills/update-user-skills")
    suspend fun postUpdateUserSkills(
        @Query("skills_ids[]") skills_ids: List<Int>
    ): SuccessResponse<Boolean>

    @POST("login")
    suspend fun postLogin(@Body data: LoginData): SuccessResponse<User>

    @POST("register")
    suspend fun postRegister(@Body data: RegisterData): SuccessResponse<User>

    @GET("organizations")
    suspend fun getOrganizations(
        @Query("page") page : Int
    ) : SuccessResponse<List<Organization>>

    @GET("organizations/{id}")
    suspend fun getOrganization(@Path("id") id: Int): SuccessResponse<Organization>

    @POST("favorites/organizations/{id}/favorite")
    suspend fun postFavoriteOrganization(@Path("id") id: Int): SuccessResponse<Boolean>

    @GET("favorites/organizations")
    suspend fun getFavoriteOrganizations(
        @Query("page") page : Int?
    ): SuccessResponse<List<Organization>>

    @GET("vacancies")
    suspend fun getVacancies(
        @Query("page") page : Int?,
        @Query("causes_id") causes: String?,
        @Query("skills_id") skills : String?,
        @Query("organization_id") organizationId: Int?
    ): SuccessResponse<List<Vacancy>>

    @GET("vacancies/{id}")
    suspend fun getVacancy(@Path("id") id: Int): SuccessResponse<Vacancy>

    @POST("applications")
    suspend fun postApplication(@Body data: VacancyApplicationData): SuccessResponse<Boolean>

    @DELETE("applications/{id}")
    suspend fun deleteApplication(@Path("id")id: Int): SuccessResponse<Boolean>

    @POST("favorites/vacancies/{id}/favorite")
    suspend fun postFavoriteVacancy(@Path("id") id: Int): SuccessResponse<Boolean>

    @GET("favorites/vacancies")
    suspend fun getFavoriteVacancies(
        @Query("page") page : Int?
    ): SuccessResponse<List<Vacancy>>

    @GET("profile")
    suspend fun getProfile(): SuccessResponse<User>

    @POST("profile/edit")
    suspend fun postEditProfile(@Body data: RegisterData): SuccessResponse<User>
}
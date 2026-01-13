package com.example.inventarislab.apiservice

import com.example.inventarislab.modeldata.Alat
import com.example.inventarislab.modeldata.Bahan
import com.example.inventarislab.modeldata.Lab
import com.example.inventarislab.modeldata.ResponseData
import com.example.inventarislab.modeldata.User
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login.php")
    suspend fun login(
        @Body requestBody: Map<String, String>
    ): ResponseData<User>

    @POST("auth/register.php")
    suspend fun register(
        @Body requestBody: Map<String, String>
    ): ResponseData<User>

    @POST("lab/read.php")
    suspend fun getLabs(): ResponseData<List<Lab>>

    // === Kelola Pengguna ===
    @POST("auth/read_lab.php")
    suspend fun getUsersByLabId(@Body requestBody: Map<String, String>): ResponseData<List<User>> // ✅ String

    @POST("auth/update.php")
    suspend fun updateUser(@Body requestBody: Map<String, String>): ResponseData<User>

    @POST("auth/delete.php")
    suspend fun deleteUser(@Body requestBody: Map<String, String>): ResponseData<String>

    // === BAHAN ===
    @POST("bahan/read.php")
    suspend fun getBahanByLabId(@Body requestBody: Map<String, String>): ResponseData<List<Bahan>>

    @POST("bahan/create.php")
    suspend fun createBahan(@Body requestBody: Map<String, String>): ResponseData<Bahan>

    @POST("bahan/update.php")
    suspend fun updateBahan(@Body requestBody: Map<String, String>): ResponseData<Bahan>

    @POST("bahan/delete.php")
    suspend fun deleteBahan(@Body requestBody: Map<String, String>): ResponseData<String>

    @POST("bahan/read_by_id.php")
    suspend fun getBahanById(@Body requestBody: Map<String, String>): ResponseData<Bahan>


    // === ALAT ===
    @POST("alat/read.php")
    suspend fun getAlatByLabId(@Body requestBody: Map<String, String>): ResponseData<List<Alat>>

    @POST("alat/create.php")
    suspend fun createAlat(@Body requestBody: Map<String, String>): ResponseData<Alat>

    @POST("alat/update.php")
    suspend fun updateAlat(@Body requestBody: Map<String, String>): ResponseData<Alat>

    @POST("alat/delete.php")
    suspend fun deleteAlat(@Body requestBody: Map<String, String>): ResponseData<String>

    @POST("alat/read_by_id.php")
    suspend fun getAlatById(@Body requestBody: Map<String, String>): ResponseData<Alat>
}
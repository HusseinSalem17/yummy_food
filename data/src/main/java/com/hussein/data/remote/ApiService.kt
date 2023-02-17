package com.hussein.data.remote

import com.hussein.domain.entity.CategoryResponse
import retrofit2.http.GET

interface ApiService {

    @GET("categories.php")
    suspend fun getMeals(): CategoryResponse
}
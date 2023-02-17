package com.hussein.data.repo

import com.hussein.data.remote.ApiService
import com.hussein.domain.entity.CategoryResponse
import com.hussein.domain.repo.MealsRepo

class MealsRepoImpl(private val apiService: ApiService) : MealsRepo {
    override suspend fun getMealsFromRemote(): CategoryResponse = apiService.getMeals()
}
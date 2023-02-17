package com.hussein.domain.repo

import com.hussein.domain.entity.CategoryResponse

interface MealsRepo {
    suspend fun getMealsFromRemote() : CategoryResponse
}
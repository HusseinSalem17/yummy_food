package com.hussein.domain.usecase

import com.hussein.domain.repo.MealsRepo

class GetMealz(private val mealsRepo: MealsRepo) {
    suspend operator fun invoke() = mealsRepo.getMealsFromRemote()
}
package com.hussein.mealz_app.di

import com.hussein.data.remote.ApiService
import com.hussein.data.repo.MealsRepoImpl
import com.hussein.domain.repo.MealsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
/*
    جوا ال RepoModule محتاج function توفرلي ال Repo بس دي مش هخليها Singleton عشان ممكن احتاج اكتر من instance in repo بس في network محتاج instance واحده فخليتهم Singleton


 */
@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    //هو كدا فهم فاي وقت هيحتاج MealsRepo هيخش علي الداله وهيرحعها عن طريق اني ببعتلك instance from apiService الي apiService هيجيبه من ال networkModule
    @Provides
    fun provideRepo(apiService: ApiService):MealsRepo{
        return MealsRepoImpl(apiService)
    }
}
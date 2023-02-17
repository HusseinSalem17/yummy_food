package com.hussein.mealz_app.di

import com.hussein.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/*
    @Module -> to let dagger know that Module u can discover it to provide Network
    @InstallIn(SingletonComponent::class) -> to make this module Singleton

    زي ما اتفقنا في DAG ان محتاج اوفر OkHttp وبعدها اوفر retrofitBuilder وبعدين اوفر ApiService عشان ال ApiService depends on retrofit and retrofit depend on OkHttp
    فمحتاج اعرف 3 functions دول الي Dagger hilt تعرفهم عشان تبدا توفرهملي
    الاول الداله دي provideOkHttp متوقع انها ترجع OkHttpClient فعشان اعرف استخدمه انا محتاج اضيف dependencies retrofit
    تااني حاجه محتاج اوفرها هي ال retrofit فعملت fun provideRetrofit ودي الي بحط فيها ال baseUrl وخلي بالك لازم ينتهي بال / وحطيت بردوا @Provides عشان كدا لو احتجت اي retrofitObject هيجيبه
    من داله provideRetrofit وهو هياخد ال parameter okHttpClient من الداله الي فوقه
    اخر حاجه بقي في networkModule اني اوفر ال RealApiService فضفت ال path in gradle(app) of domain & data عشان اعرف اوصل لل ApiService
    وخليتهم هما التلاته يبقوا Singleton عشان يبقوا ثابتين طول ال App كله
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //كدا خليت الداله دي ترجعلي OkHttpClient بس عشان اخلي retrofit تعرف انه لما تحتاج OkHttpClient ترحعه من الداله دي فعملت دا @Provides
    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}

package com.okedev.footballschedule.data.network

import com.okedev.footballschedule.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FootballService {

    private fun iniRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun <T> createService(service: Class<T>): T {
        return iniRetrofit().create(service)
    }

    private fun getBaseUrl(): String {
        return BuildConfig.BASE_URL + "/api/v1/json/" + BuildConfig.TSDB_API_KEY + "/"
    }
}
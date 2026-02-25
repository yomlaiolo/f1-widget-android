package com.yomlaiolo.f1widget.data

import com.yomlaiolo.f1widget.data.models.RaceResponse
import com.yomlaiolo.f1widget.data.models.StandingsResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface F1ApiService {
    
    @GET("{year}.json")
    suspend fun getSeasonCalendar(
        @Path("year") year: Int
    ): Response<RaceResponse>
    
    @GET("current/next.json")
    suspend fun getNextRace(): Response<RaceResponse>
    
    @GET("current/driverStandings.json")
    suspend fun getDriverStandings(): Response<StandingsResponse>
    
    @GET("current/constructorStandings.json")
    suspend fun getConstructorStandings(): Response<StandingsResponse>
    
    companion object {
        private const val BASE_URL = "http://api.jolpi.ca/ergast/f1/"
        
        fun create(): F1ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            return retrofit.create(F1ApiService::class.java)
        }
    }
}
package com.example.harrypotterapi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HpApiService {
    @GET("api/characters")
    suspend fun getAllCharacters(): Response<List<Character>>

    @GET("api/character/{id}")
    suspend fun getCharacterById(@Path("id") id: String): Response<List<Character>>

    @GET("api/characters/staff")
    suspend fun getStaff(): Response<List<Character>>

    @GET("api/characters/house/{house}")
    suspend fun getCharactersByHouse(@Path("house") house: String): Response<List<Character>>

    @GET("api/spells")
    suspend fun getAllSpells(): Response<List<Spell>>
}
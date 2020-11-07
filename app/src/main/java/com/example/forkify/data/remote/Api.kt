package com.example.forkify.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("api/search")
    suspend fun searchRecipe(
        @Query("q") query: String,
        @Query("page") page: Int
    ): Response<RecipeResponse>

    @GET("api/get")
    suspend fun getIngredients(
        @Query("rId") recipeID: String
    ):Response<RecipeIngredientsResponse>
}
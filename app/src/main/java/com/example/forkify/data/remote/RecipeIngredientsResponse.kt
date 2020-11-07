package com.example.forkify.data.remote


import com.google.gson.annotations.SerializedName

data class RecipeIngredientsResponse(
    @SerializedName("recipe")
    val ingredients: Ingredients
)
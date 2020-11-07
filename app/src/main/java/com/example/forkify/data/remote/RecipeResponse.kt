package com.example.forkify.data.remote

import com.example.forkify.data.remote.Recipe


import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("recipes")
    val recipes: List<Recipe>
)
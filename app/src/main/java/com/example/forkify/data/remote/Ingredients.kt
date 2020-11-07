package com.example.forkify.data.remote

import com.google.gson.annotations.SerializedName

data class Ingredients(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("ingredients")
    val ingredients: List<String>?,
    @SerializedName("publisher")
    val publisher: String?,
    @SerializedName("publisher_url")
    val publisherUrl: String?,
    @SerializedName("recipe_id")
    val recipeId: String?,
    @SerializedName("social_rank")
    val socialRank: Double?,
    @SerializedName("source_url")
    val sourceUrl: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("__v")
    val v: Int?
)
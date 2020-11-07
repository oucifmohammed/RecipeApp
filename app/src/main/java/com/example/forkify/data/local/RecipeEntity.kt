package com.example.forkify.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeEntity(
    val recipeId: String,
    val recipeTitle: String,
    val publisher: String,
    val imageUrl: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

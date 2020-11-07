package com.example.forkify.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {

    @Insert
    suspend fun saveRecipe(recipeEntity: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM RecipeEntity")
    fun selectAllRecipes(): LiveData<List<RecipeEntity>>
}
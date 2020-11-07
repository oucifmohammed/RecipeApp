package com.example.forkify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RecipeEntity::class],version = 1,exportSchema = false)
abstract class RecipeDataBase: RoomDatabase(){

    abstract fun recipeDao(): Dao
}
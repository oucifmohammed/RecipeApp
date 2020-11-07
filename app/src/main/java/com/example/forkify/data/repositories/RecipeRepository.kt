package com.example.forkify.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.forkify.data.local.Dao
import com.example.forkify.data.local.RecipeEntity
import com.example.forkify.data.remote.Api
import com.example.forkify.data.remote.RecipeIngredientsResponse
import com.example.forkify.data.remote.RecipePagingSource
import com.example.forkify.other.Resource
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeApi: Api,
    private val recipeDao: Dao
) {
    /**
     * Search recipes whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
//    fun getSearchResultStream(query: String): Flow<PagingData<Recipe>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 30,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = {
//                RecipePagingSource(query, recipeApi)
//            }
//        ).flow
//    }

    fun getSearchResultStream(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                RecipePagingSource(query, recipeApi)
            }
        ).liveData

    suspend fun getIngredients(recipeID: String): Resource<RecipeIngredientsResponse> {

        return try {
            val response = recipeApi.getIngredients(recipeID)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error occurred", null)
            } else {
                Resource.error("Unknown error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach the sever,check your internet connection", null)
        }
    }

    suspend fun saveRecipe(recipeEntity: RecipeEntity) {
        recipeDao.saveRecipe(recipeEntity)
    }

    suspend fun deleteRecipe(recipeEntity: RecipeEntity) {
        recipeDao.deleteRecipe(recipeEntity)
    }

    fun selectAllRecipes() = recipeDao.selectAllRecipes()
}
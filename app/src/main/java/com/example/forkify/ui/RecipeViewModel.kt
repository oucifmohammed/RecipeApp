package com.example.forkify.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.forkify.data.local.RecipeEntity
import com.example.forkify.data.remote.RecipeIngredientsResponse
import com.example.forkify.data.repositories.RecipeRepository
import com.example.forkify.other.Resource
import kotlinx.coroutines.launch

class RecipeViewModel @ViewModelInject constructor(
    private val recipeRepository: RecipeRepository
): ViewModel(){

    /**
     * Search a recipe based on a query string.
     */
    //private var currentQueryValue: String? = null

    //private var currentSearchResult: Flow<PagingData<Recipe>>? = null

    private val _recipeIngredientsLiveData = MutableLiveData<Resource<RecipeIngredientsResponse>>()
    val recipeIngredientsLiveData: LiveData<Resource<RecipeIngredientsResponse>> = _recipeIngredientsLiveData

    val recipeListLiveData: LiveData<List<RecipeEntity>> = recipeRepository.selectAllRecipes()

    val currentQuery = MutableLiveData<String>()

    var result = currentQuery.switchMap {
        recipeRepository.getSearchResultStream(it).cachedIn(viewModelScope)
    }

//    fun searchRecipe(queryString: String): Flow<PagingData<Recipe>> {
//        val lastResult = currentSearchResult
//        if (queryString == currentQueryValue && lastResult != null) {
//            return lastResult
//        }
//        currentQueryValue = queryString
//        val newResult: Flow<PagingData<Recipe>> = recipeRepository.getSearchResultStream(queryString)
//            .cachedIn(viewModelScope)
//        currentSearchResult = newResult
//        return newResult
//    }

    fun searchRecipe(query: String){
        currentQuery.value = query
    }

    fun getRecipeIngredient(recipeID: String) = viewModelScope.launch{
        _recipeIngredientsLiveData.value = Resource.loading(null)
        val response = recipeRepository.getIngredients(recipeID)
        _recipeIngredientsLiveData.value = response
    }

    fun saveRecipe(recipeEntity: RecipeEntity) = viewModelScope.launch {
        recipeRepository.saveRecipe(recipeEntity)
    }

    fun deleteRecipe(recipeEntity: RecipeEntity) = viewModelScope.launch {
        recipeRepository.deleteRecipe(recipeEntity)
    }

}
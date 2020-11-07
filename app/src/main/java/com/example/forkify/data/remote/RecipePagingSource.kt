package com.example.forkify.data.remote

import androidx.paging.PagingSource
import retrofit2.HttpException
import java.io.IOException

private const val RECIPE_STARTING_PAGE_INDEX = 1

class RecipePagingSource(
    private val query: String,
    private val api: Api
) : PagingSource<Int, Recipe>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
        val pageIndex = params.key ?: RECIPE_STARTING_PAGE_INDEX
        val response = api.searchRecipe(query, pageIndex).body()
        val items = response!!.recipes

        return try {
            LoadResult.Page(
                data = items,
                prevKey = if (pageIndex == RECIPE_STARTING_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (items.isEmpty()) null else pageIndex + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}
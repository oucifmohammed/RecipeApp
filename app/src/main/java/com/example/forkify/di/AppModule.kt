package com.example.forkify.di

import android.content.Context
import androidx.room.Room
import com.example.forkify.data.local.Dao
import com.example.forkify.data.local.RecipeDataBase
import com.example.forkify.data.remote.Api
import com.example.forkify.other.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi() =
        Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context) = Room.databaseBuilder(context,RecipeDataBase::class.java,Constants.DATABASE_NAME).build()

    @Provides
    @Singleton
    fun providesDao(database: RecipeDataBase) = database.recipeDao()
}
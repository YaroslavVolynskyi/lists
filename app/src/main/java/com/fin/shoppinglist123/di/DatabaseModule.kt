package com.fin.shoppinglist123.di

import android.content.Context
import androidx.room.Room
import com.fin.shoppinglist123.data.ShoppingListDao
import com.fin.shoppinglist123.data.ShoppingListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShoppingListDatabase =
        Room.databaseBuilder(context, ShoppingListDatabase::class.java, "shopping_list.db").build()

    @Provides
    fun provideWatchlistDao(database: ShoppingListDatabase): ShoppingListDao = database.shoppingListDao()
}
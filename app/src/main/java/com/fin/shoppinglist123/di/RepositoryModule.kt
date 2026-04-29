package com.fin.shoppinglist123.di

import com.fin.shoppinglist123.repository.ShoppingListRepository
import com.fin.shoppinglist123.repository.ShoppingListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindShoppingListRepository(impl: ShoppingListRepositoryImpl): ShoppingListRepository
}
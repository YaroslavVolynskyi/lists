package com.fin.shoppinglist123.repository

import com.fin.shoppinglist123.data.ShoppingItemEntry
import com.fin.shoppinglist123.data.ShoppingListDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingListRepositoryImpl @Inject constructor(
    private val shoppingDatabaseDao: ShoppingListDao
): ShoppingListRepository {

    override fun getShoppingList(): Flow<List<ShoppingItemEntry>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(itemId: Long): ShoppingItemEntry? {
        TODO("Not yet implemented")
    }

    override suspend fun insert(item: ShoppingItemEntry): Long {
        TODO("Not yet implemented")
    }
}
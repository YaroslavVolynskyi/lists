package com.fin.shoppinglist123.repository

import com.fin.shoppinglist123.data.ShoppingItemEntry
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {

    fun getShoppingList(): Flow<List<ShoppingItemEntry>>

    suspend fun getById(itemId: Long): ShoppingItemEntry?

    suspend fun addItem(item: ShoppingItemEntry): Long

    suspend fun updateItemName(itemId: Long, newName: String)

    suspend fun delete(itemId: Long)

    suspend fun onCheckedChanged(itemId: Long, isChecked: Boolean)

    suspend fun onExpandedChanged(itemId: Long, isExpanded: Boolean)
}
package com.fin.shoppinglist123.repository

import com.fin.shoppinglist123.data.ShoppingItemEntry
import com.fin.shoppinglist123.data.ShoppingListDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingListRepositoryImpl @Inject constructor(
    private val shoppingDatabaseDao: ShoppingListDao
): ShoppingListRepository {

    override fun getShoppingList(): Flow<List<ShoppingItemEntry>> {
        return shoppingDatabaseDao.observeAllItems()
    }

    override suspend fun getById(itemId: Long): ShoppingItemEntry? {
        return shoppingDatabaseDao.getById(itemId)
    }

    override suspend fun addItem(item: ShoppingItemEntry): Long {
        return shoppingDatabaseDao.insert(item)
    }

    override suspend fun updateItemName(itemId: Long, newName: String?) {
        newName?.let {
            shoppingDatabaseDao.updateItemName(itemId, it)
        }
    }

    override suspend fun updateItemDescription(
        itemId: Long,
        newDescription: String?
    ) {
        newDescription?.let {
            shoppingDatabaseDao.updateItemDescription(itemId, newDescription)
        }
    }

    override suspend fun delete(itemId: Long) {
        shoppingDatabaseDao.delete(itemId)
    }

    override suspend fun onCheckedChanged(itemId: Long, isChecked: Boolean) {
        shoppingDatabaseDao.onCheckedChanged(itemId, isChecked)
    }

    override suspend fun onExpandedChanged(itemId: Long, isExpanded: Boolean) {
        shoppingDatabaseDao.onExpandedChanged(itemId, isExpanded)
    }

    override fun getCheckedItems(): Flow<List<ShoppingItemEntry>> {
        return shoppingDatabaseDao.observeCheckedItems()
    }
}
package com.fin.shoppinglist123.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_items ORDER BY id ASC")
    fun observeAllItems(): Flow<List<ShoppingItemEntry>>

    @Query("SELECT * FROM shopping_items WHERE id = :itemId")
    suspend fun getById(itemId: Long): ShoppingItemEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShoppingItemEntry): Long
}
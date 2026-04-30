package com.fin.shoppinglist123.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShoppingItemEntry::class], version = 2)
abstract class ShoppingListDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao
}
package com.fin.shoppinglist123.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [ShoppingItemEntry::class], version = 1)
@TypeConverters(Converters::class)
abstract class ShoppingListDatabase: RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao
}
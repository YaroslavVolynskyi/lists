package com.fin.shoppinglist123.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItemEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val item: String,
    val description: String? = null,
    val isChecked: Boolean = false,
    val isExpanded: Boolean = false,
    val state: ShoppingItemState = ShoppingItemState.New
)

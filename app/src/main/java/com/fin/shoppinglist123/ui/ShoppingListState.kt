package com.fin.shoppinglist123.ui

import com.fin.shoppinglist123.data.ShoppingItemEntry

data class ShoppingListState(
    val items: List<ShoppingItemEntry> = emptyList(),
    val editingItemId: Long? = null,
    val editingText: String = ""
)
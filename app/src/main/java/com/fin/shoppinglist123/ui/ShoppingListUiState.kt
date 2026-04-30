package com.fin.shoppinglist123.ui

import com.fin.shoppinglist123.data.ShoppingItemEntry

data class ShoppingListUiState(
    val items: List<ShoppingItemEntry> = emptyList(),
    val expandedItemIds: Set<Long> = emptySet(),
    val currentEditedItem: EditedItem? = null
)
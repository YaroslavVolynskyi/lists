package com.fin.shoppinglist123.ui

data class EditedItem(
    val id: Long? = null,
    val isTitleText: Boolean = false,
    val isDescription: Boolean = false,
    val currentText: String? = null
)
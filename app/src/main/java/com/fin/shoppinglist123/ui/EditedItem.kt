package com.fin.shoppinglist123.ui

data class EditedItem(
    val id: Long? = null,
    val isTitleText: Boolean = true,
    var isDescription: Boolean = false,
    var currentText: String? = null
)
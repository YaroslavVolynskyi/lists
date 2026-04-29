package com.fin.shoppinglist123.data

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromState(state: ShoppingItemState): String = state.name

    @TypeConverter
    fun toState(name: String): ShoppingItemState = ShoppingItemState.valueOf(name)
}
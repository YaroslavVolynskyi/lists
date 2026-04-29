package com.fin.shoppinglist123.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.fin.shoppinglist123.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val repository: ShoppingListRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
}
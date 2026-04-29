package com.fin.shoppinglist123.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fin.shoppinglist123.data.ShoppingItemEntry
import com.fin.shoppinglist123.repository.ShoppingListRepository
import com.fin.shoppinglist123.ui.ShoppingListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val repository: ShoppingListRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

//    private val _uiState = MutableStateFlow(ShoppingListState())
//    val uiState: StateFlow<ShoppingListState> = _uiState
//
//    init {
//        viewModelScope.launch {
//            repository.getShoppingList().collect { items ->
//                _uiState.value = ShoppingListState(items = items)
//            }
//        }
//    }

    val uiState: StateFlow<ShoppingListState> = repository.getShoppingList()
        .map { itemsList -> ShoppingListState(itemsList) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ShoppingListState())

    fun addItem(item: String) {
        viewModelScope.launch {
            val idOfAdded = repository.addItem(
                ShoppingItemEntry(item = item)
            )
        }
    }

    fun onAddItem() {
        addItem("item ${Random.nextInt(1, 47)}")
    }
}
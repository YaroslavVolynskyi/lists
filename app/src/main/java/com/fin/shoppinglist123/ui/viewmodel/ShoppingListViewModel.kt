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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val repository: ShoppingListRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _editingItemId = MutableStateFlow<Long?>(null)
    private val _editingText = MutableStateFlow("")

    val uiState: StateFlow<ShoppingListState> = combine(
        repository.getShoppingList(),
        _editingItemId,
        _editingText
    ) { items, editingId, editingText ->
        ShoppingListState(
            items = items,
            editingItemId = editingId,
            editingText = editingText
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ShoppingListState())

    fun addItem(item: String) {
        viewModelScope.launch {
            repository.addItem(ShoppingItemEntry(item = item))
        }
    }

    fun onAddItem() {
        addItem("item ${Random.nextInt(1, 47)}")
    }

    fun onStartEdit(item: ShoppingItemEntry) {
        _editingItemId.value = item.id
        _editingText.value = item.item
    }

    fun onEditTextChange(text: String) {
        _editingText.value = text
    }

    fun onSaveEdit() {
        val id = _editingItemId.value ?: return
        val text = _editingText.value.trim()
        if (text.isNotEmpty()) {
            viewModelScope.launch {
                repository.updateItemName(id, text)
            }
        }
        _editingItemId.value = null
        _editingText.value = ""
    }

    fun onCancelEdit() {
        _editingItemId.value = null
        _editingText.value = ""
    }
}

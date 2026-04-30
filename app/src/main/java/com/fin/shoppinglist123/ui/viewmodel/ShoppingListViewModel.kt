package com.fin.shoppinglist123.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fin.shoppinglist123.data.ShoppingItemEntry
import com.fin.shoppinglist123.repository.ShoppingListRepository
import com.fin.shoppinglist123.ui.EditedItem
import com.fin.shoppinglist123.ui.ShoppingListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val repository: ShoppingListRepository,
) : ViewModel() {

    private val _currentEditedItem = MutableStateFlow(EditedItem())
    private val _expandedItemIds = MutableStateFlow<Set<Long>>(emptySet())

    val uiState: StateFlow<ShoppingListUiState> = combine(
        repository.getShoppingList(),
        _currentEditedItem,
        _expandedItemIds
    ) { itemsList, currentEditedItem, expandedIds ->
        ShoppingListUiState(
            items = itemsList,
            expandedItemIds = expandedIds,
            currentEditedItem = currentEditedItem
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ShoppingListUiState())

    fun onAddItem(name: String) {
        viewModelScope.launch {
            repository.addItem(ShoppingItemEntry(item = name))
        }
    }

    fun onStartEdit(item: ShoppingItemEntry, isDescription: Boolean) {
        _currentEditedItem.value = EditedItem(
            id = item.id,
            currentText = if (isDescription) item.description else item.item,
            isDescription = isDescription,
            isTitleText = !isDescription
        )
    }

    fun onEditTextChange(text: String) {
        _currentEditedItem.value = _currentEditedItem.value.copy(currentText = text)
    }

    fun onSaveEdit() {
        val edited = _currentEditedItem.value
        val id = edited.id ?: return
        val text = edited.currentText ?: return
        viewModelScope.launch {
            if (edited.isDescription) {
                repository.updateItemDescription(id, text)
            } else if (edited.isTitleText) {
                repository.updateItemName(id, text)
            }
        }
        _currentEditedItem.value = EditedItem()
    }

    fun onCancelEdit() {
        _currentEditedItem.value = EditedItem()
    }

    fun onDeleteItem(itemId: Long) {
        viewModelScope.launch {
            repository.delete(itemId)
        }
    }

    fun onToggleExpand(itemId: Long) {
        _expandedItemIds.value = _expandedItemIds.value.let { ids ->
            if (itemId in ids) ids - itemId else ids + itemId
        }
    }

    fun onCheckedChanged(itemId: Long, isChecked: Boolean) {
        viewModelScope.launch {
            repository.updateChecked(itemId, isChecked)
        }
    }
}
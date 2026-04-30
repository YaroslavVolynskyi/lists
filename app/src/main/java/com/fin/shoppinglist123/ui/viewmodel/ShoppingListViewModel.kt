package com.fin.shoppinglist123.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fin.shoppinglist123.data.ShoppingItemEntry
import com.fin.shoppinglist123.repository.ShoppingListRepository
import com.fin.shoppinglist123.ui.EditedItem
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

    private val _currentEditedItem = MutableStateFlow(EditedItem())

    val uiState: StateFlow<ShoppingListState> = combine(
        repository.getShoppingList(),
        _currentEditedItem
    ) { itemsList, currentEditedItem ->
        ShoppingListState(
            items = itemsList,
            currentEditedItem = currentEditedItem
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

    fun onStartEdit(item: ShoppingItemEntry, isDescription: Boolean) {
        _currentEditedItem.value = _currentEditedItem.value.copy(
            id = item.id,
            currentText = if (isDescription) item.description else item.item,
            isDescription = isDescription,
            isTitleText = !isDescription
        )
    }

    fun onEditTextChange(text: String, isDescription: Boolean) {
        _currentEditedItem.value = _currentEditedItem.value.copy(
            currentText = text,
//            isDescription = isDescription,
//            isTitleText = !isDescription
        )
    }

    fun onSaveEdit() {
        _currentEditedItem.value.let {
            if (it.currentText != null && it.id != null) {
                viewModelScope.launch {
                    if (it.isDescription) {
                        repository.updateItemDescription(it.id, it.currentText)
                    } else if (it.isTitleText) {
                        repository.updateItemName(it.id, it.currentText)
                    }
                }
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

    fun onToggleExpand(itemId: Long, isExpanded: Boolean) {
        viewModelScope.launch {
            repository.onExpandedChanged(itemId, isExpanded)
        }
    }

    fun onCheckedChanged(itemId: Long, isChecked: Boolean) {
        viewModelScope.launch {
            repository.onCheckedChanged(itemId, isChecked)
        }
    }
}

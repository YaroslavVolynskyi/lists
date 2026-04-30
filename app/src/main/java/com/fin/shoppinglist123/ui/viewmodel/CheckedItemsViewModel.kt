package com.fin.shoppinglist123.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fin.shoppinglist123.data.ShoppingItemEntry
import com.fin.shoppinglist123.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CheckedItemsViewModel @Inject constructor(
    repository: ShoppingListRepository
) : ViewModel() {

    val checkedItems: StateFlow<List<ShoppingItemEntry>> = repository.getCheckedItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
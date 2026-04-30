package com.fin.shoppinglist123.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fin.shoppinglist123.ui.ShoppingListUiState
import com.fin.shoppinglist123.ui.viewmodel.ShoppingListViewModel

@Composable
fun ShoppingListRoute(
    onNavigateToCheckedItems: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShoppingListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ShoppingListScreen(
        uiState = uiState,
        modifier = modifier,
        onAddItem = viewModel::onAddItem,
        onStartEdit = viewModel::onStartEdit,
        onEditTextChange = viewModel::onEditTextChange,
        onSaveEdit = viewModel::onSaveEdit,
        onCancelEdit = viewModel::onCancelEdit,
        onDelete = viewModel::onDeleteItem,
        onToggleExpand = viewModel::onToggleExpand,
        onCheckedChanged = viewModel::onCheckedChanged,
        onNavigateToCheckedItems = onNavigateToCheckedItems
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    uiState: ShoppingListUiState,
    modifier: Modifier = Modifier,
    onAddItem: (String) -> Unit,
    onStartEdit: (com.fin.shoppinglist123.data.ShoppingItemEntry, Boolean) -> Unit,
    onEditTextChange: (String) -> Unit,
    onSaveEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onDelete: (Long) -> Unit,
    onToggleExpand: (Long) -> Unit,
    onCheckedChanged: (Long, Boolean) -> Unit,
    onNavigateToCheckedItems: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Shopping list") })
        },
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = onNavigateToCheckedItems,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(Icons.Filled.Check, contentDescription = "Checked items")
                }
                FloatingActionButton(onClick = { onAddItem("New item") }) {
                    Icon(Icons.Default.Add, contentDescription = "Add item")
                }
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp
            )
        ) {
            items(uiState.items, key = { it.id }) { item ->
                val isEditingTitle = uiState.currentEditedItem?.id == item.id
                        && uiState.currentEditedItem.isTitleText
                val isEditingDescription = uiState.currentEditedItem?.id == item.id
                        && uiState.currentEditedItem.isDescription
                val isExpanded = item.id in uiState.expandedItemIds

                ShoppingItemCard(
                    item = item,
                    isEditingTitle = isEditingTitle,
                    isEditingDescription = isEditingDescription,
                    isExpanded = isExpanded,
                    editingText = uiState.currentEditedItem?.currentText ?: "",
                    onStartEdit = onStartEdit,
                    onEditTextChange = onEditTextChange,
                    onSaveEdit = onSaveEdit,
                    onCancelEdit = onCancelEdit,
                    onDelete = onDelete,
                    onToggleExpand = onToggleExpand,
                    onCheckedChanged = onCheckedChanged
                )
            }
        }
    }
}
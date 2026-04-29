package com.fin.shoppinglist123.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fin.shoppinglist123.data.ShoppingItemEntry
import com.fin.shoppinglist123.ui.viewmodel.ShoppingListViewModel


@Composable
fun ShoppingListRoute(
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
        onDelete = viewModel::onDeleteItem
    )
}

@Composable
fun ShoppingListScreen(
    uiState: ShoppingListState,
    modifier: Modifier = Modifier,
    onAddItem: () -> Unit,
    onStartEdit: (ShoppingItemEntry) -> Unit,
    onEditTextChange: (String) -> Unit,
    onSaveEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onDelete: (itemId: Long) -> Unit
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = onAddItem) {
                Icon(Icons.Default.Add, contentDescription = "Add item")
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp
            )
        ) {
            items(uiState.items, key = { item -> item.id }) { item ->
                val isEditing = uiState.editingItemId == item.id
                Card(modifier = Modifier.fillMaxWidth()) {
                    if (isEditing) {
                        val focusRequester = remember { FocusRequester() }
                        var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                            mutableStateOf(
                                TextFieldValue(
                                    text = uiState.editingText,
                                    selection = TextRange(uiState.editingText.length)
                                )
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = textFieldValue,
                                onValueChange = {
                                    textFieldValue = it
                                    onEditTextChange(it.text)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .focusRequester(focusRequester),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { onSaveEdit() })
                            )
                            IconButton(onClick = onSaveEdit) {
                                Icon(Icons.Default.Check, contentDescription = "Save")
                            }
                            IconButton(onClick = onCancelEdit) {
                                Icon(Icons.Default.Close, contentDescription = "Cancel")
                            }
                        }
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                    } else {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onStartEdit(item) }
                            .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.item,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(16.dp)
                            )
                            IconButton(onClick = {
                                onDelete.invoke(item.id)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}

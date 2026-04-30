package com.fin.shoppinglist123.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fin.shoppinglist123.data.ShoppingItemEntry
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
        onExpand = viewModel::onToggleExpand,
        onCheckedChanged = viewModel::onCheckedChanged,
        onNavigateToCheckedItems = onNavigateToCheckedItems
    )
}

@Composable
fun ShoppingListScreen(
    uiState: ShoppingListState,
    modifier: Modifier = Modifier,
    onAddItem: () -> Unit,
    onStartEdit: (ShoppingItemEntry, isDescription: Boolean) -> Unit,
    onEditTextChange: (String, isDescription: Boolean) -> Unit,
    onSaveEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onDelete: (itemId: Long) -> Unit,
    onExpand: (itemId: Long, isExpanded: Boolean) -> Unit,
    onCheckedChanged: (itemId: Long, isChecked: Boolean) -> Unit,
    onNavigateToCheckedItems: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = onNavigateToCheckedItems,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(Icons.Filled.Check, contentDescription = "Checked items")
                }
                FloatingActionButton(onClick = onAddItem) {
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
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp
            )
        ) {
            items(uiState.items, key = { item -> item.id }) { item ->
                val isEditingTitle = uiState.currentEditedItem?.id == item.id && uiState.currentEditedItem.isTitleText
                val isEditingDescription = uiState.currentEditedItem?.id == item.id && uiState.currentEditedItem.isDescription
                val isExpanded = item.isExpanded
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp)
                    ) {
                        if (isEditingTitle) {
                            val titleFocusRequester = remember { FocusRequester() }
                            var titleTextFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                                mutableStateOf(
                                    TextFieldValue(
                                        text = uiState.currentEditedItem?.currentText ?: "",
                                        selection = TextRange(uiState.currentEditedItem?.currentText?.length ?: 0)
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
                                    value = titleTextFieldValue,
                                    onValueChange = {
                                        titleTextFieldValue = it
                                        onEditTextChange(it.text, false)
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .focusRequester(titleFocusRequester),
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
                                titleFocusRequester.requestFocus()
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onStartEdit(item, false) },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val strikethroughProgress = remember { Animatable(if (item.isChecked) 1f else 0f) }
                                LaunchedEffect(item.isChecked) {
                                    strikethroughProgress.animateTo(
                                        targetValue = if (item.isChecked) 1f else 0f,
                                        animationSpec = tween(durationMillis = 300)
                                    )
                                }
                                Text(
                                    text = item.item,
                                    modifier = Modifier
                                        .weight(1f)
                                        .drawWithContent {
                                            drawContent()
                                            if (strikethroughProgress.value > 0f) {
                                                val y = size.height / 2
                                                drawLine(
                                                    color = Color.Gray,
                                                    start = Offset(0f, y),
                                                    end = Offset(size.width * strikethroughProgress.value, y),
                                                    strokeWidth = 2.dp.toPx()
                                                )
                                            }
                                        }
                                )
                                IconButton(onClick = {
                                    onExpand.invoke(item.id, !item.isExpanded)
                                }) {
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Default.ArrowCircleUp else Icons.Default.ArrowCircleDown,
                                        contentDescription = "toggle expand"
                                    )
                                }
                                IconButton(onClick = {
                                    onDelete.invoke(item.id)
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
                                Checkbox(
                                    checked = item.isChecked, onCheckedChange = { isChecked ->
                                        onCheckedChanged(item.id, isChecked)
                                    }
                                )
                            }
                        }
                        if (isExpanded) {
                            HorizontalDivider(modifier = Modifier.padding(end = 16.dp))
                            if (isEditingDescription) {
                                val descFocusRequester = remember { FocusRequester() }
                                var descTextFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                                    mutableStateOf(
                                        TextFieldValue(
                                            text = uiState.currentEditedItem?.currentText ?: "",
                                            selection = TextRange(uiState.currentEditedItem?.currentText?.length ?: 0)
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
                                        value = descTextFieldValue,
                                        onValueChange = {
                                            descTextFieldValue = it
                                            onEditTextChange(it.text, true)
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .focusRequester(descFocusRequester),
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
                                    descFocusRequester.requestFocus()
                                }
                            } else {
                                Text(
                                    text = item.description ?: "hint",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 16.dp,
                                            bottom = 16.dp
                                        )
                                        .clickable {
                                            onStartEdit(item, true)
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

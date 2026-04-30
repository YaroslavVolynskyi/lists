package com.fin.shoppinglist123.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.fin.shoppinglist123.data.ShoppingItemEntry

@Composable
fun ShoppingItemCard(
    item: ShoppingItemEntry,
    isEditingTitle: Boolean,
    isEditingDescription: Boolean,
    isExpanded: Boolean,
    editingText: String,
    onStartEdit: (ShoppingItemEntry, Boolean) -> Unit,
    onEditTextChange: (String) -> Unit,
    onSaveEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onDelete: (Long) -> Unit,
    onToggleExpand: (Long) -> Unit,
    onCheckedChanged: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            if (isEditingTitle) {
                EditableRow(
                    text = editingText,
                    onTextChange = onEditTextChange,
                    onSave = onSaveEdit,
                    onCancel = onCancelEdit
                )
            } else {
                ItemTitleRow(
                    item = item,
                    isExpanded = isExpanded,
                    onStartEdit = { onStartEdit(item, false) },
                    onToggleExpand = { onToggleExpand(item.id) },
                    onDelete = { onDelete(item.id) },
                    onCheckedChanged = { onCheckedChanged(item.id, it) }
                )
            }
            if (isExpanded) {
                HorizontalDivider(modifier = Modifier.padding(end = 16.dp))
                if (isEditingDescription) {
                    EditableRow(
                        text = editingText,
                        onTextChange = onEditTextChange,
                        onSave = onSaveEdit,
                        onCancel = onCancelEdit
                    )
                } else {
                    Text(
                        text = item.description ?: "hint",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp)
                            .clickable { onStartEdit(item, true) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EditableRow(
    text: String,
    onTextChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(
                text = text,
                selection = TextRange(text.length)
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
                onTextChange(it.text)
            },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSave() })
        )
        IconButton(onClick = onSave) {
            Icon(Icons.Default.Check, contentDescription = "Save")
        }
        IconButton(onClick = onCancel) {
            Icon(Icons.Default.Close, contentDescription = "Cancel")
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun ItemTitleRow(
    item: ShoppingItemEntry,
    isExpanded: Boolean,
    onStartEdit: () -> Unit,
    onToggleExpand: () -> Unit,
    onDelete: () -> Unit,
    onCheckedChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onStartEdit() },
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
        IconButton(onClick = onToggleExpand) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.ArrowCircleUp else Icons.Default.ArrowCircleDown,
                contentDescription = "toggle expand"
            )
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChanged
        )
    }
}

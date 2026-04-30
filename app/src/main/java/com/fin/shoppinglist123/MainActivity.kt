package com.fin.shoppinglist123

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.fin.shoppinglist123.ui.CheckedItemsRoute
import com.fin.shoppinglist123.ui.ShoppingListRoute
import com.fin.shoppinglist123.ui.navigation.CheckedItemsKey
import com.fin.shoppinglist123.ui.navigation.ShoppingListKey
import com.fin.shoppinglist123.ui.theme.Shoppinglist123Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Shoppinglist123Theme {
                val backStack = remember { mutableStateListOf<Any>(ShoppingListKey) }

                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = { key ->
                        when (key) {
                            is ShoppingListKey -> NavEntry(key) {
                                ShoppingListRoute(
                                    onNavigateToCheckedItems = {
                                        backStack.add(CheckedItemsKey)
                                    }
                                )
                            }
                            is CheckedItemsKey -> NavEntry(key) {
                                CheckedItemsRoute(
                                    onBack = { backStack.removeLastOrNull() }
                                )
                            }
                            else -> NavEntry(key) {}
                        }
                    }
                )
            }
        }
    }
}
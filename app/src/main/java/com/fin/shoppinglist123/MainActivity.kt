package com.fin.shoppinglist123

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.fin.shoppinglist123.ui.screens.CheckedItemsRoute
import com.fin.shoppinglist123.ui.screens.ShoppingListRoute
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
                val backStack = rememberNavBackStack(ShoppingListKey)

                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = entryProvider {
                        entry<ShoppingListKey> {
                            ShoppingListRoute(
                                onNavigateToCheckedItems = {
                                    backStack.add(CheckedItemsKey)
                                }
                            )
                        }
                        entry<CheckedItemsKey> {
                            CheckedItemsRoute(
                                onBack = { backStack.removeLastOrNull() }
                            )
                        }
                    }
                )
            }
        }
    }
}
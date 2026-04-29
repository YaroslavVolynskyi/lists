package com.fin.shoppinglist123

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fin.shoppinglist123.ui.ShoppingListRoute
import com.fin.shoppinglist123.ui.theme.Shoppinglist123Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Shoppinglist123Theme {
                ShoppingListRoute()
            }
        }
    }
}


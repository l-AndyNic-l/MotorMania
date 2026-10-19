package com.example.motormania.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.motormania.viewmodel.ProductoViewModel

@Composable
fun ListaProductosScreen(
    viewModel: ProductoViewModel
) {
    val productos by viewModel.productos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.obtenerProductos()
    }

    LazyColumn() {
        items(productos) { producto ->
            Text(text = "Código: ${producto.codigo}")
            Text(text = "Nombre: ${producto.nombre}")
            Text(text = "Precio: ${producto.precio}")
        }
    }
}
package com.example.motormania.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motormania.model.Producto
import com.example.motormania.repository.ProveedoresRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductoApiViewModel: ViewModel() {
    private val repository: ProveedoresRepository = ProveedoresRepository()
    private val _productosList = MutableStateFlow<List<Producto>>(emptyList())
    val productosList: StateFlow<List<Producto>> = _productosList

    fun fetchProductos(id: Long) {
        viewModelScope.launch {
            try {
                _productosList.value = repository.getProductos(id)

            } catch (e: Exception) {
                println("Error al obtener datos: ${e.localizedMessage}")
            }
        }
    }
}
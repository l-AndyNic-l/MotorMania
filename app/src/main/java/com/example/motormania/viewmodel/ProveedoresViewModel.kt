package com.example.motormania.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motormania.model.Proveedor
import com.example.motormania.repository.ProveedoresRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProveedoresViewModel: ViewModel() {

    private val repository: ProveedoresRepository = ProveedoresRepository()
    private val _proveedoresList = MutableStateFlow<List<Proveedor>>(emptyList())
    val proveedoresList: StateFlow<List<Proveedor>> = _proveedoresList

    init {
        fetchProveedores()
    }

    private fun fetchProveedores() {
        viewModelScope.launch {
            try {
                _proveedoresList.value = repository.getProveedores()

            } catch (e: Exception) {
                println("Error al obtener datos: ${e.localizedMessage}")
            }
        }
    }
}
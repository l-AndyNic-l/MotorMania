package com.example.motormania.model

data class ProductoUiState (
    val codigo: String = "",
    val img: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val precio: String = "",
    val cantidad: String = "",
    val stockMinimo: String = "",
    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false,

    val errores: ProductoErrores = ProductoErrores()
)
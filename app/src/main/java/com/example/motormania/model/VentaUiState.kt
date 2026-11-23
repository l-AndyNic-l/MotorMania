package com.example.motormania.model

data class VentaUiState (
    val codigo: String = "",
    val usuario: String = "",
    val boleta: String = "",
    val producto: String = "",
    val cantidad: String = "",
    val total: String = "",
    val estado: String = "",
    val guardadoExitosamente: Boolean = false,

    val errores: VentaErrores = VentaErrores()
)

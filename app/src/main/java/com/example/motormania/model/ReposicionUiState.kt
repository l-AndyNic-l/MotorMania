package com.example.motormania.model

data class ReposicionUiState (
    val codigo: String = "",
    val usuario: String = "",
    val producto: String = "",
    val cantidad: String = "",
    val guardadoExitosamente: Boolean = false,

    val errores: ReposicionErrores = ReposicionErrores()
)
package com.example.guia_22.model

data class UsuarioUiState (
    val nombre: String = "",
    val apellido: String = "",
    val nickname: String = "",
    val fechaNacimiento: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val promociones: Boolean = false,

    val errores: UsuarioErrores = UsuarioErrores()
)
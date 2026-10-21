package com.example.motormania.model

data class LoginUiState (
    val correo: String = "",
    val clave: String = "",
    val logeado: String = "",

    val errores: LoginErrores = LoginErrores()
)
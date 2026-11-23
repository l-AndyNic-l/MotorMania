package com.example.motormania.model

data class Producto (
    val id: Long,
    val img: String,
    val nombre: String,
    val categoria: String,
    val precio: Int,
    val idProveedor: Long
)
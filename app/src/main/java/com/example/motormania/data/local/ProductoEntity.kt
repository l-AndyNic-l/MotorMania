package com.example.motormania.data.local

import androidx.room.Entity

@Entity(tableName = "productos")
data class ProductoEntity (
    val codigo: Int,
    val img: String,
    val nombre: String,
    val categoria: String,
    val precio: Int,
    val cantidad: Int,
    val stockMinimo: Int
)
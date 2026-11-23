package com.example.motormania.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ventas")
data class VentaEntity (
    @PrimaryKey
    val codigo: Int,

    val usuario: Int,
    val boleta: Int,
    val producto: Int,
    val cantidad: Int,
    val total: Int,
    val estado: String
)
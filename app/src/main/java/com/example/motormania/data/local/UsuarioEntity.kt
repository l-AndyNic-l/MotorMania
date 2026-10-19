package com.example.guia_22.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["correo"], unique = true)]
)
data class UsuarioEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nombre: String,
    val apellido: String,
    val nickname: String,
    val fechaNacimiento: String,
    val correo: String,
    val clave: String,
    val direccion: String,
    val promociones: String
)
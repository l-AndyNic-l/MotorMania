package com.example.motormania.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reposiciones")
data class ReposicionEntity (
    @PrimaryKey
    val codigo: Int,

    val usuario: Int,
    val producto: Int,
    val cantidad: Int
)
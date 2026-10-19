package com.example.guia_22.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertar(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios")
    suspend fun obtenerTodos(): List<UsuarioEntity>

    @Query("SELECT * FROM usuarios WHERE id = :cod LIMIT 1")
    suspend fun obtenerPorId(cod: Int): UsuarioEntity?

    @Update
    suspend fun actualizar(producto: UsuarioEntity)

    @Delete
    suspend fun eliminar(producto: UsuarioEntity)
}
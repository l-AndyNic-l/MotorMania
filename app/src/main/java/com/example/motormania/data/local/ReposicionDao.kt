package com.example.motormania.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReposicionDao {
    @Insert
    suspend fun insertar(reposicion: ReposicionEntity)

    @Query("SELECT * FROM reposiciones")
    suspend fun obtenerTodos(): List<ReposicionEntity>

    @Query("SELECT * FROM reposiciones WHERE codigo = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): ReposicionEntity?

    @Update
    suspend fun actualizar(reposicion: ReposicionEntity)

    @Query("DELETE FROM reposiciones WHERE codigo = :codigo")
    suspend fun eliminarPorCodigo(codigo: Int)
}
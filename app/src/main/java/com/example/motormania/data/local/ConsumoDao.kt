package com.example.motormania.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ConsumoDao {
    @Insert
    suspend fun insertar(consumo: ConsumoEntity)

    @Query("SELECT * FROM consumos")
    suspend fun obtenerTodos(): List<ConsumoEntity>

    @Query("SELECT * FROM consumos WHERE codigo = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): ConsumoEntity?

    @Update
    suspend fun actualizar(consumo: ConsumoEntity)

    @Query("DELETE FROM consumos WHERE codigo = :codigo")
    suspend fun eliminarPorCodigo(codigo: Int)
}
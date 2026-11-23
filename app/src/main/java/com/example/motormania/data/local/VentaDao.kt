package com.example.motormania.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface VentaDao {
    @Insert
    suspend fun insertar(venta: VentaEntity)

    @Query("SELECT * FROM ventas")
    suspend fun obtenerTodos(): List<VentaEntity>

    @Query("SELECT * FROM ventas WHERE codigo = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): VentaEntity?

    @Update
    suspend fun actualizar(venta: VentaEntity)

    @Query("DELETE FROM ventas WHERE codigo = :codigo")
    suspend fun eliminarPorCodigo(codigo: Int)
}
package com.example.motormania.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductoDao {

    @Insert
    suspend fun insertar(producto: ProductoEntity)

    @Query("SELECT * FROM productos")
    suspend fun obtenerTodos(): List<ProductoEntity>

    @Query("SELECT * FROM productos WHERE codigo = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): ProductoEntity?

    @Update
    suspend fun actualizar(producto: ProductoEntity)

    @Delete
    suspend fun eliminar(producto: ProductoEntity)
}
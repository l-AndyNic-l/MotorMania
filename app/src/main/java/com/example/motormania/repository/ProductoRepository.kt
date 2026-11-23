package com.example.motormania.repository

import com.example.motormania.data.local.ProductoDao
import com.example.motormania.data.local.ProductoEntity
import com.example.motormania.model.ProductoUiState

class ProductoRepository(private val dao: ProductoDao) {

    suspend fun guardar(producto: ProductoUiState) =
        dao.insertar(producto.toEntity())

    suspend fun obtenerTodos(): List<ProductoUiState> =
        dao.obtenerTodos().map { it.toUiState() }

    suspend fun obtenerTodosStockMinimo(): List<ProductoUiState> =
        dao.obtenerTodosStockMinimo().map { it.toUiState() }

    suspend fun obtenerPorUbicacionExacta(zona: String, repisa: Int, estante: Int): List<ProductoUiState> =
        dao.obtenerPorUbicacionExacta(zona, repisa, estante).map { it.toUiState() }

    suspend fun obtenerPorId(codigo: Int): ProductoUiState? =
        dao.obtenerPorId(codigo)?.toUiState()

    suspend fun actualizar(producto: ProductoUiState) =
        dao.actualizar(producto.toEntity())

    suspend fun eliminar(codigo: Int) =
        dao.eliminarPorCodigo(codigo)

}

private fun ProductoEntity.toUiState() = ProductoUiState(
    codigo = codigo.toString(),
    img = img,
    nombre = nombre,
    categoria = categoria,
    precio = precio.toString(),
    cantidad = cantidad.toString(),
    stockMinimo = stockMinimo.toString(),
    stockObjetivo = stockObjetivo.toString(),
    zona = zona,
    repisa = repisa.toString(),
    estante = estante.toString()
)

private fun ProductoUiState.toEntity() = ProductoEntity(
    codigo = codigo.toInt(),
    img = img,
    nombre = nombre,
    categoria = categoria,
    precio = precio.toInt(),
    cantidad = cantidad.toInt(),
    stockMinimo = stockMinimo.toInt(),
    stockObjetivo = stockMinimo.toInt(),
    zona = zona.uppercase(),
    repisa = repisa.toInt(),
    estante = estante.toInt()
)
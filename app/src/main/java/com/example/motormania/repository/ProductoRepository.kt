package com.example.motormania.repository

import com.example.motormania.data.local.ProductoDao
import com.example.motormania.data.local.ProductoEntity
import com.example.motormania.model.ProductoUiState

class ProductoRepository(private val dao: ProductoDao) {

    suspend fun guardar(producto: ProductoUiState) {
        val entity = ProductoEntity(
            codigo = producto.codigo.toInt(),
            img = producto.img,
            nombre = producto.nombre,
            categoria = producto.categoria,
            precio = producto.precio.toInt(),
            cantidad = producto.cantidad.toInt(),
            stockMinimo = producto.stockMinimo.toInt()
        )
        dao.insertar(entity)
    }

    suspend fun obtenerTodos(): List<ProductoUiState> =
        dao.obtenerTodos().map { it.toUiState() }

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
    stockMinimo = stockMinimo.toString()
)

private fun ProductoUiState.toEntity() = ProductoEntity(
    codigo = codigo.toInt(),
    img = img,
    nombre = nombre,
    categoria = categoria,
    precio = precio.toInt(),
    cantidad = cantidad.toInt(),
    stockMinimo = stockMinimo.toInt()
)
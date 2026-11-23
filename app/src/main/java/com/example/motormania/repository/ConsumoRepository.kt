package com.example.motormania.repository

import androidx.room.withTransaction
import com.example.motormania.data.local.AppDataBase
import com.example.motormania.data.local.ConsumoEntity
import com.example.motormania.model.ConsumoUiState

class ConsumoRepository (
    private val db: AppDataBase
) {
    private val dao = db.consumoDao()
    private val productoDao = db.productoDao()

    suspend fun obtenerTodos() : List<ConsumoUiState> =
        dao.obtenerTodos().map { it.toUiState() }

    suspend fun obtenerPorId(codigo: Int): ConsumoUiState? =
        dao.obtenerPorId(codigo)?.toUiState()

    suspend fun actualizar(consumo: ConsumoUiState) =
        dao.actualizar(consumo.toEntity())

    suspend fun eliminar(codigo: Int) =
        dao.eliminarPorCodigo(codigo)

    suspend fun guardarConsumoYConsumir(consumo: ConsumoUiState): String {
        val codigoProducto = consumo.producto.toIntOrNull() ?: return "Producto inválido"
        val cantidad = consumo.cantidad.toIntOrNull() ?: return "Cantidad inválida"

        return try {
            db.withTransaction {
                val producto = productoDao.obtenerPorId(codigoProducto)
                    ?: throw Exception("No encontrado")

                if (producto.cantidad < cantidad) throw Exception("Cantidad inválida")

                productoDao.actualizar(
                    producto.copy(cantidad = producto.cantidad - cantidad)
                )
                dao.insertar(consumo.toEntity())
            }
            "Actualizado"
        } catch (e: Exception) {
            e.message ?: "Error al guardar"
        }
    }
}

private fun ConsumoEntity.toUiState() = ConsumoUiState(
    codigo = codigo.toString(),
    usuario = usuario.toString(),
    producto = producto.toString(),
    cantidad = cantidad.toString()
)
private fun ConsumoUiState.toEntity() = ConsumoEntity(
    codigo = codigo.toInt(),
    usuario = usuario.toInt(),
    producto = producto.toInt(),
    cantidad = cantidad.toInt()
)
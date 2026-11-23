package com.example.motormania.repository

import androidx.room.withTransaction
import com.example.motormania.data.local.AppDataBase
import com.example.motormania.data.local.ReposicionEntity
import com.example.motormania.model.ReposicionUiState

class ReposicionRepository (
    private val db: AppDataBase
) {
    private val dao = db.reposicionDao()
    private val productoDao = db.productoDao()

    suspend fun obtenerTodos() : List<ReposicionUiState> =
        dao.obtenerTodos().map { it.toUiState() }

    suspend fun obtenerPorId(codigo: Int): ReposicionUiState? =
        dao.obtenerPorId(codigo)?.toUiState()

    suspend fun actualizar(reposicion: ReposicionUiState) =
        dao.actualizar(reposicion.toEntity())

    suspend fun eliminar(codigo: Int) =
        dao.eliminarPorCodigo(codigo)

    suspend fun guardarVentaYReponer(reposicion: ReposicionUiState): String {
        val codigoProducto = reposicion.producto.toIntOrNull() ?: return "Producto inválido"
        val cantidad = reposicion.cantidad.toIntOrNull() ?: return "Cantidad inválida"

        return try {
            db.withTransaction {
                val producto = productoDao.obtenerPorId(codigoProducto)
                    ?: throw Exception("No encontrado")

                productoDao.actualizar(
                    producto.copy(cantidad = producto.cantidad + cantidad)
                )
                dao.insertar(reposicion.toEntity())
            }
            "Actualizado"
        } catch (e: Exception) {
            e.message ?: "Error al guardar"
        }
    }
}

private fun ReposicionEntity.toUiState() = ReposicionUiState(
    codigo = codigo.toString(),
    usuario = usuario.toString(),
    producto = producto.toString(),
    cantidad = cantidad.toString()
)
private fun ReposicionUiState.toEntity() = ReposicionEntity(
    codigo = codigo.toInt(),
    usuario = usuario.toInt(),
    producto = producto.toInt(),
    cantidad = cantidad.toInt()
)
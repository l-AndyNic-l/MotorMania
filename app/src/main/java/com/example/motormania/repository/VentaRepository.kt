package com.example.motormania.repository

import androidx.room.withTransaction
import com.example.motormania.data.local.AppDataBase
import com.example.motormania.data.local.VentaEntity
import com.example.motormania.model.VentaUiState

class VentaRepository (
    private val db: AppDataBase
) {

    private val dao = db.ventaDao()
    private val productoDao = db.productoDao()

    suspend fun obtenerTodos() : List<VentaUiState> =
        dao.obtenerTodos().map { it.toUiState() }

    suspend fun obtenerPorId(codigo: Int): VentaUiState? =
        dao.obtenerPorId(codigo)?.toUiState()

    suspend fun actualizar(venta: VentaUiState) =
        dao.actualizar(venta.toEntity())

    suspend fun eliminar(codigo: Int) =
        dao.eliminarPorCodigo(codigo)

    suspend fun guardarVentaYDescontar(venta: VentaUiState): String {
        val codigoProducto = venta.producto.toIntOrNull() ?: return "Producto inválido"
        val cantidad = venta.cantidad.toIntOrNull() ?: return "Cantidad inválida"

        return try {
            db.withTransaction {
                val producto = productoDao.obtenerPorId(codigoProducto)
                    ?: throw Exception("No encontrado")

                if (producto.cantidad < cantidad) throw Exception("Cantidad inválida")

                productoDao.actualizar(
                    producto.copy(cantidad = producto.cantidad - cantidad)
                )
                dao.insertar(venta.toEntity())
            }
            "Actualizado"
        } catch (e: Exception) {
            e.message ?: "Error al guardar"
        }
    }
}

private fun VentaEntity.toUiState() = VentaUiState(
    codigo = codigo.toString(),
    usuario = usuario.toString(),
    boleta = boleta.toString(),
    producto = producto.toString(),
    cantidad = cantidad.toString(),
    total = total.toString(),
    estado = estado
)
private fun VentaUiState.toEntity() = VentaEntity(
    codigo = codigo.toInt(),
    usuario = usuario.toInt(),
    boleta = boleta.toInt(),
    producto = producto.toInt(),
    cantidad = cantidad.toInt(),
    total = total.toInt(),
    estado = estado
)

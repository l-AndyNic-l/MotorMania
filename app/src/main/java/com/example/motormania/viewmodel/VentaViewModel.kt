package com.example.motormania.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.motormania.data.local.AppDataBase
import com.example.motormania.model.VentaErrores
import com.example.motormania.model.VentaUiState
import com.example.motormania.repository.ProductoRepository
import com.example.motormania.repository.VentaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VentaViewModel (app: Application): AndroidViewModel(app) {

    private val db = AppDataBase.getDataBase(app)
    private val productoDao = db.productoDao()

    private val productoRepo = ProductoRepository(productoDao)
    private val ventaRepo = VentaRepository(db)

    private val _estado = MutableStateFlow(VentaUiState())
    val estado: StateFlow<VentaUiState> = _estado

    private val _ventas = MutableStateFlow<List<VentaUiState>>(emptyList())
    val ventas: StateFlow<List<VentaUiState>> = _ventas

    fun onCodigoChange(valor: String) {
        _estado.update {
            it.copy(
                codigo = valor,
                errores = it.errores.copy(codigo = null)
            )
        }
    }

    fun onUsuarioChange(valor: String) {
        _estado.update {
            it.copy(
                usuario = valor,
                errores = it.errores.copy(usuario = null)
            )
        }
    }

    fun onBoletaChange(valor: String) {
        _estado.update {
            it.copy(
                boleta = valor,
                errores = it.errores.copy(boleta = null)
            )
        }
    }

    fun onProductoChange(valor: String) {
        _estado.update {
            it.copy(
                producto = valor,
                errores = it.errores.copy(producto = null)
            )
        }
    }

    fun onCantidadChange(valor: String) {
        _estado.update {
            it.copy(
                cantidad = valor,
                errores = it.errores.copy(cantidad = null)
            )
        }
    }

    fun onTotalChange(valor: String) {
        _estado.update {
            it.copy(
                total = valor,
                errores = it.errores.copy(total = null)
            )
        }
    }

    fun onEstadoChange(valor: String) {
        _estado.update {
            it.copy(
                estado = valor,
                errores = it.errores.copy(estado = null)
            )
        }
    }

    fun onUpdateGuardadoExitoso(valor: Boolean) {
        _estado.update {
            it.copy(
                guardadoExitosamente = valor,
            )

        }
    }

    suspend fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        val codigo = estadoActual.codigo.toIntOrNull()
        val usuario = estadoActual.usuario.toIntOrNull()
        val boleta = estadoActual.boleta.toIntOrNull()
        val producto = estadoActual.producto.toIntOrNull()
        val cantidad = estadoActual.cantidad.toIntOrNull()
        val total = estadoActual.total.toIntOrNull()

        var errores = VentaErrores(
            codigo = when {
                estadoActual.codigo.isBlank() -> "Campo obligatorio"
                codigo == null || codigo <= 0 -> "Código no válido"
                else -> null
            },
            usuario = when {
                estadoActual.usuario.isBlank() -> "Campo obligatorio"
                usuario == null || usuario <= 0 -> "Usuario no válido"
                else -> null
            },
            boleta = when {
                estadoActual.boleta.isBlank() -> "Campo obligatorio"
                boleta == null || boleta <= 0 -> "Boleta no válido"
                else -> null
            },
            producto = when {
                estadoActual.producto.isBlank() -> "Campo obligatorio"
                producto == null || producto <= 0 -> "Producto no válido"
                else -> null
            },
            cantidad = when {
                estadoActual.cantidad.isBlank() -> "Campo obligatorio"
                cantidad == null || cantidad <= 0 -> "Debe ser una cantidad válida"
                else -> null
            },
            total = when {
                estadoActual.total.isBlank() -> "Campo obligatorio"
                total == null || total < 0 -> "Debe ser un total válido"
                else -> null
            },
            estado = if (estadoActual.estado.isBlank()) "Campo obligatorio" else null
        )

        if (listOfNotNull(
                errores.codigo, errores.usuario, errores.boleta,
                errores.producto, errores.cantidad, errores.total, errores.estado
            ).isNotEmpty()
        ) {
            _estado.update { it.copy(errores = errores) }
            return false
        }

        val productoDb = try {
            productoRepo.obtenerPorId(producto!!)
        } catch (e: Exception) {
            null
        }

        if (productoDb == null) {
            errores = errores.copy(producto = "Producto no encontrado")
            _estado.update { it.copy(errores = errores) }
            return false
        }

        if (productoDb.cantidad.toInt() < cantidad!!) {
            errores = errores.copy(
                cantidad = "Stock insuficiente (disponible: ${productoDb.cantidad})"
            )
            _estado.update { it.copy(errores = errores) }
            return false
        }

        _estado.update { it.copy(errores = VentaErrores()) }
        return true
    }

    fun limpiarCampos() {
        _estado.update {
            it.copy(
                codigo = "",
                usuario = "",
                boleta = "",
                producto = "",
                cantidad = "",
                total = "",
                estado = ""
            )
        }
    }

    fun guardar() {
        viewModelScope.launch {
            if (!validarFormulario()) return@launch

            val resultado = ventaRepo.guardarVentaYDescontar(_estado.value)

            if (resultado == "Actualizado") {
                onUpdateGuardadoExitoso(true)
                limpiarCampos()
                obtenerVentas()
            } else {
                _estado.update {
                    it.copy(errores = it.errores.copy(
                        producto = if (resultado == "No encontrado") "Producto no encontrado" else null,
                        cantidad = if (resultado == "Cantidad inválida") "Stock insuficiente" else null
                    ))
                }
            }
        }
    }

    fun obtenerVentas() {
        viewModelScope.launch {
            try {
                _ventas.value = ventaRepo.obtenerTodos()
            } catch (e: Exception) {
                println("Error al obtener las ventas: ${e.message}")
            }
        }
    }

    fun obtenerVenta(codigo: String) {
        viewModelScope.launch {
            try {
                val venta = ventaRepo.obtenerPorId(codigo.toInt())

                if (venta != null) {
                    _estado.value = venta.copy()

                } else {
                    println("Venta ${codigo} no encontrada")
                }

            } catch (e: Exception) {
                println("Error al obtener venta: ${e.message}")
            }
        }
    }

    fun actualizarVenta() {
        val a = _estado.value

        viewModelScope.launch {
            try {
                ventaRepo.actualizar(a)
                onUpdateGuardadoExitoso(true)

            } catch (e: Exception) {
                println("Error de Ejecución: ${e.message}")
            }
        }
    }

    fun eliminarVenta(codigo: String) {
        viewModelScope.launch {
            try {
                ventaRepo.eliminar(codigo.toInt())
                obtenerVentas()

            } catch (e: Exception) {
                println("Error al eliminar venta: ${e.message}")
            }
        }
    }
}


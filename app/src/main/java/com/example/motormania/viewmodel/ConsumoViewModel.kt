package com.example.motormania.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.motormania.data.local.AppDataBase
import com.example.motormania.model.ConsumoErrores
import com.example.motormania.model.ConsumoUiState
import com.example.motormania.repository.ConsumoRepository
import com.example.motormania.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConsumoViewModel (app: Application): AndroidViewModel(app) {
    private val db = AppDataBase.getDataBase(app)
    private val productoDao = db.productoDao()
    private val productoRepo = ProductoRepository(productoDao)
    private val consumosRepo = ConsumoRepository(db)

    private val _estado = MutableStateFlow(ConsumoUiState())
    val estado: StateFlow<ConsumoUiState> = _estado

    private val _consumos = MutableStateFlow<List<ConsumoUiState>>(emptyList())
    val consumos: StateFlow<List<ConsumoUiState>> = _consumos

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
        val producto = estadoActual.producto.toIntOrNull()
        val cantidad = estadoActual.cantidad.toIntOrNull()

        var errores = ConsumoErrores(
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

            producto = when {
                estadoActual.producto.isBlank() -> "Campo obligatorio"
                producto == null || producto <= 0 -> "Producto no válido"
                else -> null
            },
            cantidad = when {
                estadoActual.cantidad.isBlank() -> "Campo obligatorio"
                cantidad == null || cantidad <= 0 -> "Debe ser una cantidad válida"
                else -> null
            }
        )

        if (listOfNotNull(
                errores.codigo,
                errores.usuario,
                errores.producto,
                errores.cantidad
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

        _estado.update { it.copy(errores = ConsumoErrores()) }
        return true
    }

    fun limpiarCampos() {
        _estado.update {
            it.copy(
                codigo = "",
                usuario = "",
                producto = "",
                cantidad = ""
            )
        }
    }

    fun guardar() {
        viewModelScope.launch {
            if (!validarFormulario()) return@launch

            val resultado = consumosRepo.guardarConsumoYConsumir(_estado.value)

            if (resultado == "Actualizado") {
                onUpdateGuardadoExitoso(true)
                limpiarCampos()
                obtenerConsumos()
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

    fun obtenerConsumos() {
        viewModelScope.launch {
            try {
                _consumos.value = consumosRepo.obtenerTodos()
            } catch (e: Exception) {
                println("Error al obtener los consumos: ${e.message}")
            }
        }
    }

    fun obtenerConsumo(codigo: String) {
        viewModelScope.launch {
            try {
                val consumo = consumosRepo.obtenerPorId(codigo.toInt())

                if (consumo != null) {
                    _estado.value = consumo.copy()

                } else {
                    println("Consumo ${codigo} no encontrado")
                }

            } catch (e: Exception) {
                println("Error al obtener consumo: ${e.message}")
            }
        }
    }

    fun actualizarVenta() {
        val a = _estado.value

        viewModelScope.launch {
            try {
                consumosRepo.actualizar(a)
                onUpdateGuardadoExitoso(true)

            } catch (e: Exception) {
                println("Error de Ejecución: ${e.message}")
            }
        }
    }

    fun eliminarVenta(codigo: String) {
        viewModelScope.launch {
            try {
                consumosRepo.eliminar(codigo.toInt())
                obtenerConsumos()

            } catch (e: Exception) {
                println("Error al eliminar consumo: ${e.message}")
            }
        }
    }
}
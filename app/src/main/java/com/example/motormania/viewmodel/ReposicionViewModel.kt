package com.example.motormania.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.motormania.data.local.AppDataBase
import com.example.motormania.model.ReposicionErrores
import com.example.motormania.model.ReposicionUiState
import com.example.motormania.repository.ProductoRepository
import com.example.motormania.repository.ReposicionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReposicionViewModel (app: Application): AndroidViewModel(app) {
    private val db = AppDataBase.getDataBase(app)
    private val productoDao = db.productoDao()
    private val productoRepo = ProductoRepository(productoDao)
    private val reposicionRepo = ReposicionRepository(db)

    private val _estado = MutableStateFlow(ReposicionUiState())
    val estado: StateFlow<ReposicionUiState> = _estado

    private val _reposiciones = MutableStateFlow<List<ReposicionUiState>>(emptyList())
    val reposiciones: StateFlow<List<ReposicionUiState>> = _reposiciones

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

        var errores = ReposicionErrores(
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

        _estado.update { it.copy(errores = ReposicionErrores()) }
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

            val resultado = reposicionRepo.guardarVentaYReponer(_estado.value)

            if (resultado == "Actualizado") {
                onUpdateGuardadoExitoso(true)
                limpiarCampos()
                obtenerReposiciones()
            } else {
                _estado.update {
                    it.copy(errores = it.errores.copy(
                        producto = if (resultado == "No encontrado") "Producto no encontrado" else null
                    ))
                }
            }
        }
    }

    fun obtenerReposiciones() {
        viewModelScope.launch {
            try {
                _reposiciones.value = reposicionRepo.obtenerTodos()
            } catch (e: Exception) {
                println("Error al obtener las reposiciones: ${e.message}")
            }
        }
    }

    fun obtenerReposicion(codigo: String) {
        viewModelScope.launch {
            try {
                val reposicion = reposicionRepo.obtenerPorId(codigo.toInt())

                if (reposicion != null) {
                    _estado.value = reposicion.copy()

                } else {
                    println("Reposicion ${codigo} no encontrada")
                }

            } catch (e: Exception) {
                println("Error al obtener reposicion: ${e.message}")
            }
        }
    }

    fun actualizarVenta() {
        val a = _estado.value

        viewModelScope.launch {
            try {
                reposicionRepo.actualizar(a)
                onUpdateGuardadoExitoso(true)

            } catch (e: Exception) {
                println("Error de Ejecución: ${e.message}")
            }
        }
    }

    fun eliminarVenta(codigo: String) {
        viewModelScope.launch {
            try {
                reposicionRepo.eliminar(codigo.toInt())
                obtenerReposiciones()

            } catch (e: Exception) {
                println("Error al eliminar reposición: ${e.message}")
            }
        }
    }
}
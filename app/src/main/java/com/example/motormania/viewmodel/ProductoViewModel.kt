package com.example.motormania.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.motormania.data.local.AppDataBase
import com.example.motormania.model.ProductoErrores
import com.example.motormania.model.ProductoUiState
import com.example.motormania.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductoViewModel(app: Application): AndroidViewModel(app) {

    private val dao = AppDataBase.getDataBase(app).productoDao()
    private val _estado = MutableStateFlow(ProductoUiState())
    val estado: StateFlow<ProductoUiState> = _estado

    private val _productos = MutableStateFlow<List<ProductoUiState>>(emptyList())
    val productos: StateFlow<List<ProductoUiState>> = _productos

    fun onCodigoChange(valor: String) {
        _estado.update {
            it.copy(
                codigo = valor,
                errores = it.errores.copy(codigo = null)
            )
        }
    }

    fun onImgChange(valor: String) {
        _estado.update {
            it.copy(
                img = valor,
                errores = it.errores.copy(img = null)
            )
        }
    }

    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(
                nombre = valor,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    fun onCategoriaChange(valor: String) {
        _estado.update {
            it.copy(
                categoria = valor,
                errores = it.errores.copy(categoria = null)
            )
        }
    }

    fun onPrecioChange(valor: String) {
        _estado.update {
            it.copy(
                precio = valor,
                errores = it.errores.copy(precio = null)
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

    fun onStockMinimoChange(valor: String) {
        _estado.update {
            it.copy(
                stockMinimo = valor,
                errores = it.errores.copy(stockMinimo = null)
            )
        }
    }

    fun onStockObjetivo(valor: String) {
        _estado.update {
            it.copy(
                stockObjetivo = valor,
                errores = it.errores.copy(stockObjetivo = null)
            )
        }
    }

    fun onZonaChange(valor: String) {
        _estado.update {
            it.copy(
                zona = valor,
                errores = it.errores.copy(zona = null)
            )
        }
    }

    fun onRepisaChange(valor: String) {
        _estado.update {
            it.copy(
                repisa = valor,
                errores = it.errores.copy(repisa = null)
            )
        }
    }

    fun onEstanteChange(valor: String) {
        _estado.update {
            it.copy(
                estante = valor,
                errores = it.errores.copy(estante = null)
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

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        val codigo = estadoActual.codigo.toIntOrNull()
        val precio = estadoActual.precio.toIntOrNull()
        val cantidad = estadoActual.cantidad.toIntOrNull()
        val stockMinimo = estadoActual.stockMinimo.toIntOrNull()
        val stockObjetivo = estadoActual.stockObjetivo.toIntOrNull()
        val repisa = estadoActual.repisa.toIntOrNull()
        val estante = estadoActual.estante.toIntOrNull()

        val errores = ProductoErrores(
            codigo = when {
                estadoActual.codigo.isBlank() -> "Campo obligatorio"
                codigo == null || codigo <= 0 -> "Código no válido"
                else -> null
            },

            img = if (estadoActual.img.isBlank())
                "Campo obligatorio" else null,

            nombre = if (estadoActual.nombre.isBlank())
                "Campo obligatorio" else null,

            categoria = if (estadoActual.categoria.isBlank())
                "Campo obligatorio" else null,

            precio = when {
                estadoActual.precio.isBlank() -> "Campo obligatorio"
                precio == null || precio < 0 -> "Debe ser un monto válido"
                else -> null
            },

            cantidad = when {
                estadoActual.cantidad.isBlank() -> "Campo obligatorio"
                cantidad == null || cantidad < 0 -> "Debe ser una cantidad válida"
                else -> null
            },

            stockMinimo = when {
                estadoActual.stockMinimo.isBlank() -> "Campo obligatorio"
                stockMinimo == null || stockMinimo < 0 ->
                    "Debe ser un stock mínimo válido"
                else -> null
            },

            stockObjetivo = when {
                estadoActual.stockObjetivo.isBlank() -> "Campo obligatorio"
                stockObjetivo == null || stockObjetivo < estadoActual.stockMinimo.toInt() ->
                    "Debe ser un stock objetivo mayor al minímo"
                else -> null
            },

            zona = if (estadoActual.zona.isBlank()) {
                "Campo obligatorio"
            } else if (estadoActual.zona.uppercase() !in listOf("A", "B", "C", "D")) {
                "Las zonas deben ser A, B, C o D"
            } else null,

            repisa = when {
                estadoActual.repisa.isBlank() -> "Campo obligatorio"
                repisa == null -> "Debe ser una repisa válida"
                estadoActual.repisa.toInt() !in 1..5 -> "Las repisas son de 1 a 5"
                else -> null
            },

            estante = when {
                estadoActual.estante.isBlank() -> "Campo obligatorio"
                estante == null -> "Debe ser una repisa válida"
                estadoActual.estante.toInt() !in 1..9 -> "Los estantes son de 1 a 9"
                else -> null
            }

        )

        _estado.update {
            it.copy(errores = errores)
        }

        return listOfNotNull(
            errores.codigo,
            errores.img,
            errores.nombre,
            errores.categoria,
            errores.precio,
            errores.cantidad,
            errores.stockMinimo,
            errores.stockObjetivo,
            errores.zona,
            errores.repisa,
            errores.estante
        ).isEmpty()
    }

    fun limpiarCampos() {
        _estado.update {
            it.copy(
                codigo = "",
                img = "",
                nombre = "",
                categoria = "",
                precio = "",
                cantidad = "",
                stockMinimo = "",
                stockObjetivo = "",
                zona = "",
                repisa = "",
                estante = ""
            )
        }
    }

    fun guardar() {
        val a = _estado.value

        viewModelScope.launch {
            try {
                ProductoRepository(dao).guardar(a)
                onUpdateGuardadoExitoso(true)

            } catch (e: Exception) {
                println("Error de Ejecución: ${e.message}")
            }
        }
    }

    fun obtenerProductos() {
        viewModelScope.launch {
            try {
                _productos.value = ProductoRepository(dao).obtenerTodos()
            } catch (e: Exception) {
                println("Error al obtener productos: ${e.message}")
            }
        }
    }

    fun obtenerTodosStockMinimo() {
        viewModelScope.launch {
            try {
                _productos.value = ProductoRepository(dao).obtenerTodosStockMinimo()
            } catch (e: Exception) {
                println("Error al obtener productos: ${e.message}")
            }
        }
    }

    fun obtenerPorUbicacionExacta(zona: String, repisa: String, estante: String) {
        viewModelScope.launch {
            try {
                _productos.value = ProductoRepository(dao).obtenerPorUbicacionExacta(zona, repisa.toInt(), estante.toInt())
            } catch (e: Exception) {
                println("Error al obtener productos: ${e.message}")
            }
        }
    }


    fun obtenerProducto(codigo: String) {
        viewModelScope.launch {
            try {
                val producto = ProductoRepository(dao).obtenerPorId(codigo.toInt())

                if (producto != null) {
                    _estado.value = producto.copy()

                } else {
                    println("Producto ${codigo} no encontrado")
                }

            } catch (e: Exception) {
                println("Error al obtener producto: ${e.message}")
            }
        }
    }

    fun actualizarProducto() {
        val a = _estado.value

        viewModelScope.launch {
            try {
                ProductoRepository(dao).actualizar(a)
                onUpdateGuardadoExitoso(true)

            } catch (e: Exception) {
                println("Error de Ejecución: ${e.message}")
            }
        }
    }

    fun eliminarProducto(codigo: String) {
        viewModelScope.launch {
            try {
                ProductoRepository(dao).eliminar(codigo.toInt())
                obtenerProductos()

            } catch (e: Exception) {
                println("Error al obtener producto: ${e.message}")
            }
        }
    }
}
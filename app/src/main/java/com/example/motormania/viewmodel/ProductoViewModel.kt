package com.example.motormania.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.motormania.model.ProductoErrores
import com.example.motormania.model.ProductoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ProductoViewModel(app: Application): AndroidViewModel(app) {

    private val _estado = MutableStateFlow(ProductoUiState())
    val estado: StateFlow<ProductoUiState> = _estado

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

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        val errores = ProductoErrores(
            codigo = if (estadoActual.codigo.toInt() <= 0) "Código no válido" else null,
            img = if (estadoActual.img.isBlank()) "Campo obligatorio" else null,
            nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
            categoria = if (estadoActual.categoria.isBlank()) "Campo obligatorio" else null,
            precio = if (estadoActual.precio.toInt() < 0) "Debe ser un monto válido" else null,
            cantidad = if (estadoActual.cantidad.toInt() < 0) "Debe ser una cantidad válida" else null,
            stockMinimo = if (estadoActual.stockMinimo.toInt() < 0) "Debe ser un stock mínimo válido" else null
        )

        _estado.update {
            it.copy(
                errores = errores
            )
        }

        val hayErrores = listOfNotNull(
            errores.codigo,
            errores.img,
            errores.nombre,
            errores.categoria,
            errores.precio,
            errores.cantidad,
            errores.stockMinimo
        ).isNotEmpty()

        return !hayErrores
    }
}
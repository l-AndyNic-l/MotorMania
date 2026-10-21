package com.example.motormania.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia_22.model.UsuarioErrores
import com.example.guia_22.model.UsuarioUiState
import com.example.motormania.data.local.AppDataBase
import com.example.motormania.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(app: Application): AndroidViewModel(app) {

    private val dao = AppDataBase.getDataBase(app).usuarioDao()
    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado

    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(
                nombre = valor,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    fun onApellidoChange(valor: String) {
        _estado.update {
            it.copy(
                apellido = valor,
                errores = it.errores.copy(apellido = null)
            )
        }
    }

    fun onNicknameChange(valor: String) {
        _estado.update {
            it.copy(
                nickname = valor,
                errores = it.errores.copy(nickname = null)
            )
        }
    }

    fun onFechaNacimientoChange(valor: String) {
        _estado.update {
            it.copy(
                fechaNacimiento = valor,
                errores = it.errores.copy(fechaNacimiento = null)
            )
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(
                correo = valor,
                errores = it.errores.copy(correo = null)
            )
        }
    }

    fun onClaveChange(valor: String) {
        _estado.update {
            it.copy(
                clave = valor,
                errores = it.errores.copy(clave = null)
            )
        }
    }

    fun onDireccionChange(valor: String) {
        _estado.update {
            it.copy(
                direccion = valor,
                errores = it.errores.copy(direccion = null)
            )
        }
    }

    fun onPromociones(valor: Boolean) {
        _estado.update {
            it.copy(
                promociones = valor
            )
        }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
            apellido = if (estadoActual.apellido.isBlank()) "Campo obligatorio" else null,
            nickname = if (estadoActual.nickname.length <= 3) "Debe tener al menos 3 caracteres" else null,
            correo = if (!estadoActual.correo.contains("@duocuc.cl") && !estadoActual.correo.contains("@gmail.com")) "Correo inválido" else null,
            fechaNacimiento = if (estadoActual.fechaNacimiento.isBlank()) "Campo obligatorio" else null,
            clave = if (estadoActual.clave.length < 6) "Debe tener al menos 6 caracteres" else null,
            direccion = if (estadoActual.direccion.isBlank()) "Campo obligatorio" else null
        )

        _estado.update {
            it.copy(
                errores = errores
            )
        }

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.apellido,
            errores.nickname,
            errores.fechaNacimiento,
            errores.correo,
            errores.clave,
            errores.direccion
        ).isNotEmpty()

        return !hayErrores
    }

    fun limpiarCampos() {
        _estado.update {
            it.copy(
                nombre = "",
                apellido = "",
                nickname = "",
                fechaNacimiento = "",
                correo = "",
                clave = "",
                direccion = ""
            )
        }
    }

    fun guardar(onListo: () -> Unit) {
        val a = _estado.value

        viewModelScope.launch {
            try {
                UsuarioRepository(dao).guardar(a)
                onListo()

            } catch (e: Exception) {
                println("Error de Ejecución: ${e.message}")
            }
        }
    }
}
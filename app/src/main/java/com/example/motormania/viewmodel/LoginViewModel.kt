package com.example.motormania.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.motormania.data.local.AppDataBase
import com.example.motormania.model.LoginErrores
import com.example.motormania.model.LoginUiState
import com.example.motormania.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(app: Application): AndroidViewModel(app) {

    private val dao = AppDataBase.getDataBase(app).usuarioDao()
    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado


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

    fun onLogeadoChange(valor: String) {
        _estado.update {
            it.copy(
                logeado = valor
            )
        }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        val errores = LoginErrores(
            correo = if (!estadoActual.correo.contains("@duocuc.cl") && !estadoActual.correo.contains("@gmail.com")) "Correo inválido" else null,
            clave = if (estadoActual.clave.length < 6) "Debe tener al menos 6 caracteres" else null
        )

        _estado.update {
            it.copy(
                errores = errores
            )
        }

        val hayErrores = listOfNotNull(
            errores.correo,
            errores.clave,
        ).isNotEmpty()

        return !hayErrores
    }

    fun limpiarCampos() {
        _estado.update {
            it.copy(
                correo = "",
                clave = ""
            )
        }
    }

    fun iniciarSesion() {
        val a = _estado.value

        viewModelScope.launch {
            try {
                val usuario = UsuarioRepository(dao).obtenerPorCorreo(a.correo)

                if (usuario != null) {
                    if (usuario.clave == a.clave) {
                        onLogeadoChange("LOGEADO")

                    } else {
                        onLogeadoChange("CREDENCIALES ERRONEAS")
                    }
                } else {
                    onLogeadoChange("NO ENCONTRADO")
                }

            } catch (e: Exception) {
                println("Error al obtener producto: ${e.message}")
            }
        }
    }
}
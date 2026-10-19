package com.example.motormania.repository

import com.example.guia_22.data.local.UsuarioDao
import com.example.guia_22.data.local.UsuarioEntity
import com.example.guia_22.model.UsuarioUiState

class UsuarioRepository(private val dao: UsuarioDao) {

    suspend fun guardar(usuario: UsuarioUiState) {
        val entity = UsuarioEntity(
            nombre = usuario.nombre,
            apellido = usuario.apellido,
            nickname = usuario.nickname,
            fechaNacimiento = usuario.fechaNacimiento,
            correo = usuario.correo,
            clave = usuario.clave,
            direccion = usuario.direccion,
            promociones = if (usuario.promociones) "Suscrito" else "No Suscrito"
        )
        dao.insertar(entity)
    }

    suspend fun obtenerTodos(): List<UsuarioUiState> =
        dao.obtenerTodos().map { it.toUiState() }

    suspend fun obtenerPorId(correo: String): UsuarioUiState? =
        dao.obtenerPorId(correo)?.toUiState()

    suspend fun actualizar(usuario: UsuarioUiState) =
        dao.actualizar(usuario.toEntity())

    suspend fun eliminar(usuario: UsuarioUiState) =
        dao.eliminar(usuario.toEntity())

}

private fun UsuarioEntity.toUiState() = UsuarioUiState(
    nombre = nombre,
    apellido = apellido,
    nickname = nickname,
    fechaNacimiento = fechaNacimiento,
    correo = correo,
    clave = clave,
    direccion = direccion,
    promociones = promociones == "Suscrito"
)

private fun UsuarioUiState.toEntity() = UsuarioEntity(
    nombre = nombre,
    apellido = apellido,
    nickname = nickname,
    fechaNacimiento = fechaNacimiento,
    correo = correo,
    clave = clave,
    direccion = direccion,
    promociones = if (promociones) "Suscrito" else "No Suscrito"
)
package com.example.motormania.repository

import com.example.motormania.data.remote.RetrofitInstance
import com.example.motormania.model.Producto
import com.example.motormania.model.Proveedor

class ProveedoresRepository {

    suspend fun getProveedores(): List<Proveedor> {
        return RetrofitInstance.api.getProveedores()
    }

    suspend fun getProductos(id: Long): List<Producto> {
        return RetrofitInstance.api.getProductos(id)
    }
}
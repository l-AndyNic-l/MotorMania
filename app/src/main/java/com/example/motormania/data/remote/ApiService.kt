package com.example.motormania.data.remote

import com.example.motormania.model.Producto
import com.example.motormania.model.Proveedor
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("proveedores")
    suspend fun getProveedores(): List<Proveedor>

    @GET("productos/proveedor/{id}")
    suspend fun getProductos(@Path("id") id: Long): List<Producto>
}
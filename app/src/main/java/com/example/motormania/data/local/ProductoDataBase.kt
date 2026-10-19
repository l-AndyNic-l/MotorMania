package com.example.motormania.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.guia_22.data.local.UsuarioDao

@Database(entities = [ProductoEntity::class], version = 1)
abstract class ProductoDataBase: RoomDatabase() {

    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile
        private var INSTANCE: ProductoDataBase? = null

        fun getDataBase(context: Context): ProductoDataBase {
            return INSTANCE?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ProductoDataBase::class.java,
                    "producto_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
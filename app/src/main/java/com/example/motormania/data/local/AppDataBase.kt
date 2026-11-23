package com.example.motormania.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.guia_22.data.local.UsuarioDao
import com.example.guia_22.data.local.UsuarioEntity

@Database(
    entities = [
        UsuarioEntity::class,
        ProductoEntity::class,
        VentaEntity::class,
        ReposicionEntity::class,
        ConsumoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao

    abstract fun ventaDao(): VentaDao

    abstract fun reposicionDao(): ReposicionDao

    abstract fun consumoDao(): ConsumoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDataBase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "motormania_database"
                )
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
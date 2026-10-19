package com.example.motormania.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.motormania.view.InicioSecionScreen
import com.example.motormania.view.ListaProductosScreen
import com.example.motormania.view.MainScreen
import com.example.motormania.view.ProductoScreen
import com.example.motormania.view.RegistroScreen
import com.example.motormania.viewmodel.LoginViewModel
import com.example.motormania.viewmodel.ProductoViewModel
import com.example.motormania.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "inicio-sesion"
    ){
        composable(route = "registro") {
            RegistroScreen(
                navController,
                usuarioViewModel
            )
        }

        composable(route = "inicio-sesion") {
            InicioSecionScreen(
                navController,
                loginViewModel
            )
        }

        composable(route = "registrar-producto") {
            ProductoScreen(
                navController,
                productoViewModel
            )
        }

        composable(route = "inicio") {
            MainScreen(
                navController
            )
        }

        composable(route = "lista-productos") {
            ListaProductosScreen(
                productoViewModel
            )
        }
    }
}
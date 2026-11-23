package com.example.motormania.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.motormania.view.producto.EditarScreen
import com.example.motormania.view.login.InicioSecionScreen
import com.example.motormania.view.producto.ListaProductosEditarScreen
import com.example.motormania.view.producto.ListaProductosScreen
import com.example.motormania.view.MainScreen
import com.example.motormania.view.producto.ProductoScreen
import com.example.motormania.view.producto.QrScannerScreen
import com.example.motormania.view.login.RegistroScreen
import com.example.motormania.view.movimiento.ConsumoScreen
import com.example.motormania.view.movimiento.EditarVentaScreen
import com.example.motormania.view.movimiento.ListaMovimientosEditarScreen
import com.example.motormania.view.movimiento.ListaMovimientosScreen
import com.example.motormania.view.movimiento.MovimientosScreen
import com.example.motormania.view.movimiento.QrScannerMovimientoScreen
import com.example.motormania.view.movimiento.ReposicionScreen
import com.example.motormania.view.movimiento.VentaScreen
import com.example.motormania.view.reposicion.ListaProductosReposicionScreen
import com.example.motormania.view.reposicion.ListarProductosProveedorScreen
import com.example.motormania.view.reposicion.ListarProveedores
import com.example.motormania.view.ubicacion.ListaProductosUbicacionEditar
import com.example.motormania.view.ubicacion.ListaProductosUbicacionScreen
import com.example.motormania.view.ubicacion.ProductoUbicacionScreen
import com.example.motormania.view.ubicacion.QrScannerUbicacionScreen
import com.example.motormania.viewmodel.ConsumoViewModel
import com.example.motormania.viewmodel.LoginViewModel
import com.example.motormania.viewmodel.ProductoApiViewModel
import com.example.motormania.viewmodel.ProductoViewModel
import com.example.motormania.viewmodel.ProveedoresViewModel
import com.example.motormania.viewmodel.QrViewModel
import com.example.motormania.viewmodel.ReposicionViewModel
import com.example.motormania.viewmodel.UsuarioViewModel
import com.example.motormania.viewmodel.VentaViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()
    val ventaViewModel: VentaViewModel = viewModel()
    val reposicionViewModel: ReposicionViewModel = viewModel()
    val consumoViewModel: ConsumoViewModel = viewModel()
    val proveedoresViewModel: ProveedoresViewModel = viewModel()
    val productoApiViewModel: ProductoApiViewModel = viewModel()
    val qrViewModel: QrViewModel = viewModel()

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
                navController,
                productoViewModel
            )
        }

        composable(route = "lista-productos-editar") {
            ListaProductosEditarScreen(
                navController,
                productoViewModel
            )
        }

        composable(route = "editar-producto") {
            EditarScreen(
                navController,
                productoViewModel
            )
        }

        composable("escaner-qr") {
            QrScannerScreen(
                viewModel = qrViewModel,
                onBack = { navController.popBackStack() },
                productoViewModel,
                navController
            )
        }

        composable("lista-movimientos") {
            ListaMovimientosScreen(
                navController,
                ventaViewModel,
                reposicionViewModel,
                consumoViewModel
            )
        }

        composable("registrar-movimiento") {
            MovimientosScreen(
                navController
            )
        }

        composable("registrar-venta") {
            VentaScreen(
                navController,
                ventaViewModel,
                productoViewModel
            )
        }

        composable(route = "lista-movimientos-editar") {
            ListaMovimientosEditarScreen(
                navController,
                ventaViewModel
            )
        }

        composable(route = "editar-venta") {
            EditarVentaScreen(
                navController,
                ventaViewModel
            )
        }

        composable("movimiento-escaner-qr") {
            QrScannerMovimientoScreen(
                viewModel = qrViewModel,
                onBack = { navController.popBackStack() },
                ventaViewModel,
                navController
            )
        }

        composable("registrar-reposicion") {
            ReposicionScreen(
                navController,
                reposicionViewModel,
                productoViewModel
            )
        }

        composable("registrar-consumo") {
            ConsumoScreen(
                navController,
                consumoViewModel,
                productoViewModel
            )
        }

        composable(route = "lista-productos-stockminimo") {
            ListaProductosReposicionScreen(
                navController,
                productoViewModel
            )
        }

        composable(route = "lista-productos-ubicacion") {
            ListaProductosUbicacionScreen(
                navController,
                productoViewModel
            )
        }

        composable(route = "lista-productos-ubicacion-editar") {
            ListaProductosUbicacionEditar(
                navController,
                productoViewModel
            )
        }

        composable(route = "editar-ubicacion") {
            ProductoUbicacionScreen(
                navController,
                productoViewModel
            )
        }
        composable("ubicacion-escaner-qr") {
            QrScannerUbicacionScreen(
                viewModel = qrViewModel,
                onBack = { navController.popBackStack() },
                productoViewModel,
                navController
            )
        }

        composable(route = "lista-proveedores") {
            ListarProveedores(
                navController,
                proveedoresViewModel
            )
        }

        composable(
            route = "catalogo-proveedor/{idProveedor}",
            arguments = listOf(navArgument("idProveedor") { type = NavType.LongType })
        ) { backStackEntry ->
            val idProveedor = backStackEntry.arguments?.getLong("idProveedor")

            ListarProductosProveedorScreen(
                navController,
                productoApiViewModel,
                idProveedor
            )
        }

    }
}
package com.example.motormania.view.movimiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.motormania.viewmodel.ProductoViewModel
import com.example.motormania.viewmodel.ReposicionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReposicionScreen (
    navController: NavController,
    viewModel: ReposicionViewModel,
    productoViewModel: ProductoViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        productoViewModel.obtenerProductos()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("inicio") {
                                popUpTo("inicio") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio"
                        )
                    }
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "MotorMania - Movimientos")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate("inicio-sesion") {
                                popUpTo(navController.graph.id) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        },

        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("lista-movimientos") },
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Flecha izquierda y derecha",
                                modifier = Modifier.padding(bottom = 0.dp)
                            )
                        }
                    },
                    label = { Text("Movimientos") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { navController.navigate("registrar-movimiento") },
                    icon = { Icon(Icons.Default.AddBox, contentDescription = "Agregar") },
                    label = { Text("Agregar") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("lista-movimientos-editar") },
                    icon = { Icon(Icons.Default.Edit, contentDescription = "Editar") },
                    label = { Text("Editar") }
                )
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Registro Reposición",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp)
            )

            OutlinedTextField(
                value = estado.codigo,
                onValueChange = viewModel::onCodigoChange,
                label = { Text(text = "Código (ej: 123):") },
                isError = estado.errores.codigo != null,
                supportingText = {
                    estado.errores.codigo?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.usuario,
                onValueChange = viewModel::onUsuarioChange,
                label = { Text(text = "Usuario (ej: 123):") },
                isError = estado.errores.usuario != null,
                supportingText = {
                    estado.errores.usuario?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.producto,
                onValueChange = viewModel::onProductoChange,
                label = { Text(text = "Producto (ej: 123):") },
                isError = estado.errores.producto != null,
                supportingText = {
                    estado.errores.producto?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.cantidad,
                onValueChange = viewModel::onCantidadChange,
                label = { Text(text = "Cantidad:") },
                isError = estado.errores.cantidad != null,
                supportingText = {
                    estado.errores.cantidad?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    scope.launch {
                        if (viewModel.validarFormulario()) {
                            viewModel.guardar()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(text = "Crear Reposición")
            }

            if (estado.guardadoExitosamente) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.onUpdateGuardadoExitoso(false)
                    },
                    title = {
                        Text("¡Reposición guardada!")
                    },
                    text = {
                        Text("La reposición se ha guardado exitosamente.")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.onUpdateGuardadoExitoso(false)
                                navController.navigate("lista-movimientos")
                                viewModel.limpiarCampos()
                            }
                        ) {
                            Text("Aceptar")
                        }
                    }
                )
            }
        }
    }
}
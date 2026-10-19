package com.example.motormania.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.motormania.viewmodel.ProductoViewModel

@Composable
fun ProductoScreen (
    navController: NavController,
    viewModel: ProductoViewModel
) {
    val estado by viewModel.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro Producto",
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
            label = { Text(text = "Código (ej: 001):") },
            isError = estado.errores.codigo != null,
            supportingText = {
                estado.errores.codigo?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.img,
            onValueChange = viewModel::onImgChange,
            label = { Text(text = "Img (ej: https):") },
            isError = estado.errores.img != null,
            supportingText = {
                estado.errores.img?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.nombre,
            onValueChange = viewModel::onNombreChange,
            label = { Text(text = "Nombre:") },
            isError = estado.errores.nombre != null,
            supportingText = {
                estado.errores.nombre?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.categoria,
            onValueChange = viewModel::onCategoriaChange,
            label = { Text(text = "Categoria:") },
            isError = estado.errores.categoria != null,
            supportingText = {
                estado.errores.categoria?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.precio,
            onValueChange = viewModel::onPrecioChange,
            label = { Text(text = "Precio $:") },
            isError = estado.errores.precio != null,
            supportingText = {
                estado.errores.precio?.let {
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

        OutlinedTextField(
            value = estado.stockMinimo,
            onValueChange = viewModel::onStockMinimoChange,
            label = { Text(text = "Stock Mínimo:") },
            isError = estado.errores.stockMinimo != null,
            supportingText = {
                estado.errores.stockMinimo?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (viewModel.validarFormulario()) {

                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(text = "Crear Producto")
        }
    }
}
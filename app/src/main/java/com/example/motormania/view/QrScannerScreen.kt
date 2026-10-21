package com.example.motormania.view

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.motormania.utils.CameraPermissionHelper
import com.example.motormania.utils.QrScanner
import com.example.motormania.viewmodel.ProductoViewModel
import com.example.motormania.viewmodel.QrViewModel

@Composable
fun QrScannerScreen(
    viewModel: QrViewModel,
    onBack: () -> Unit = {},
    viewModelProducto: ProductoViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val qrResult by viewModel.qrResult.observeAsState()

    var hasCameraPermission by remember {
        mutableStateOf(CameraPermissionHelper.hasCameraPermission(context))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (!granted) {
            Toast.makeText(
                context,
                "Se necesita permiso de cámara para escanear QR",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Limpiar resultado al entrar para no mostrar QR viejo
    LaunchedEffect(Unit) {
        viewModel.clearResult()
    }

    // Pedir permiso automáticamente si no lo hay
    LaunchedEffect(hasCameraPermission) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // BackHandler para volver con el botón atrás del sistema
    androidx.activity.compose.BackHandler { onBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            !hasCameraPermission -> {
                Text(
                    "Permiso de cámara requerido",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "Para escanear códigos QR, necesitamos acceso a la cámara",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Button(onClick = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Conceder permiso de cámara")
                }
            }

            qrResult == null -> {
                Text(
                    "Escanea un código QR",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    QrScanner(
                        onQrCodeScanned = { qrContent ->
                            viewModel.onQrDetected(qrContent)
                            Toast.makeText(
                                context,
                                "QR detectado!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Marco guía, sin Surface transparente
                    Box(
                        modifier = Modifier
                            .size(250.dp)
                            .align(Alignment.Center)
                            .border(
                                border = BorderStroke(
                                    2.dp,
                                    MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                    )
                }

                Spacer(Modifier.height(16.dp))
                Text(
                    "Enfoca el código QR en el marco central",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "La cámara se activa automáticamente",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))
                TextButton(onClick = onBack) {
                    Text("Volver")
                }
            }

            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "QR Detectado:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Button(
                            onClick = {
                                viewModelProducto.obtenerProducto(qrResult.orEmpty())
                                navController.navigate("editar-producto")
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Editar Producto")
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar Producto",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.clearResult() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Escanear otro código QR")
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Volver")
                    }
                }
            }
        }
    }
}
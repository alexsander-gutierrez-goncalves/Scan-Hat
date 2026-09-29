package br.com.alexsander.leitor.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import br.com.alexsander.leitor.ROUTE
import br.com.alexsander.leitor.compose.CameraPreview
import br.com.alexsander.leitor.compose.ClipBoardModal
import br.com.alexsander.leitor.compose.FlashlightIcon
import br.com.alexsander.leitor.data.Code
import br.com.alexsander.leitor.viewmodel.CodeViewModel
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning

fun NavHostController.navigateToHome() {
    navigate(ROUTE.FIRST.name, navOptions {
        popUpTo(ROUTE.SECOND.name) {
            inclusive = true
        }
    })
}

fun NavGraphBuilder.homeScreen(barcodeScanner: BarcodeScanner, viewModel: CodeViewModel, copy: (String) -> Unit = {}) {
    composable(ROUTE.FIRST.name)
    {
        HomeScreen(barcodeScanner,viewModel::insert, copy)
    }
}

@Composable
fun HomeScreen(barcodeScanner: BarcodeScanner = BarcodeScanning.getClient(),onRead: (Code) -> Unit = { }, copy: (String) -> Unit = { }) {
    val context = LocalContext.current
    val managedActivityResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}
    val cameraController = remember { LifecycleCameraController(context) }
    cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    var code by remember { mutableStateOf<Code?>(null) }
    val torchEnabled = remember { mutableStateOf(false) }
    LaunchedEffect(barcodeScanner) {
        managedActivityResultLauncher.launch(android.Manifest.permission.CAMERA)

        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            MlKitAnalyzer(
                listOf(barcodeScanner),
                COORDINATE_SYSTEM_VIEW_REFERENCED,
                ContextCompat.getMainExecutor(context)
            ) { result: MlKitAnalyzer.Result? ->
                result?.getValue(barcodeScanner)?.forEach { r ->
                    if (code == null) {
                        code = Code(value = r.rawValue.toString())
                        onRead(code!!)
                    }
                }
            }
        )
    }

    DisposableEffect(barcodeScanner ) {
        onDispose {
            barcodeScanner.close() // Libera o scanner ao sair
        }
    }

    Box {
        CameraPreview(cameraController, Modifier.fillMaxSize())
        FlashlightIcon(torchEnabled, cameraController)
        if (code != null) {
            ClipBoardModal(code, {
                copy(it)
                code = null
            }) {
                code = null
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
package br.com.alexsander.leitor.compose

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier
)
{
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier= modifier,
        factory = { context -> PreviewView(context) },
        update = { previewView ->
            previewView.controller = controller
            controller.bindToLifecycle(lifecycleOwner)
        }
    )
}
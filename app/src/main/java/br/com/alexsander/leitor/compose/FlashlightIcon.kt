package br.com.alexsander.leitor.compose

import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FlashlightOff
import androidx.compose.material.icons.rounded.FlashlightOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import br.com.alexsander.leitor.R

@Composable
fun BoxScope.FlashlightIcon(
    torchEnabled: MutableState<Boolean>,
    cameraController: LifecycleCameraController
) {
    IconButton(
        onClick = {
            torchEnabled.value = !torchEnabled.value
            cameraController.enableTorch(torchEnabled.value)
        },
        Modifier.align(Alignment.Center),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = Color(0.5f, 0.5f, 0.5f, 0.5f)
        )
    ) {
        Icon(
            imageVector = if (torchEnabled.value) Icons.Rounded.FlashlightOff else Icons.Rounded.FlashlightOn,
            contentDescription = stringResource(id = R.string.flashlight_icon)
        )
    }
}
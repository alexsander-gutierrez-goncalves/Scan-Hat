package br.com.alexsander.leitor.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.alexsander.leitor.R

@Composable
fun QRCode(qrCodeImage: ImageBitmap?, onClickDownload:() -> Unit) {
    if(qrCodeImage != null) {
        Image(
            modifier = Modifier.size(300.dp),
            bitmap = qrCodeImage,
            contentDescription = "QR Code"
        )
        Button(
            onClickDownload,
            Modifier.height(48.dp)
        ) {
            Text(stringResource(R.string.download))
            Icon(
                Icons.Filled.Download,
                ""
            )
        }
    }
}
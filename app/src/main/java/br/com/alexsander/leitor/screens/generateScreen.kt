package br.com.alexsander.leitor.screens

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import br.com.alexsander.leitor.R
import br.com.alexsander.leitor.ROUTE
import br.com.alexsander.leitor.compose.QRCode
import br.com.alexsander.leitor.viewmodel.CodeViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun NavHostController.navigateToGenerate() {
    navigate(ROUTE.THIRD.name, navOptions {
        popUpTo(ROUTE.FIRST.name) {
            inclusive = true
        }
    })
}

fun NavGraphBuilder.generateScreen(viewModel: CodeViewModel) {
    composable(ROUTE.THIRD.name)
    {
        GenerateScreen(viewModel.qrCodeImage, viewModel::generateQRCode)
    }
}

fun shareQrCodeImage(context: Context, qrCodeImage: ImageBitmap) {
    val filename = "qr_code${System.currentTimeMillis()}.png"
    val file = File(context.cacheDir, filename)
    try {
        val outputStream = FileOutputStream(file)
        qrCodeImage.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        val bitmapUri =
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, bitmapUri)
        }
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.qr_code_share_title)
            )
        )
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

@Composable
fun GenerateScreen(
    qrCodeImage: ImageBitmap? = null,
    generateQRCode: (String, Float) -> Unit
) {
    var textFieldValue by rememberSaveable { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val canGenerate = textFieldValue.isNotEmpty()
    val density = LocalDensity.current.density
    fun onValueChange(it: String) {
        textFieldValue = it
    }

    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue,
            placeholder = { Text(stringResource(R.string.type_here)) },
            onValueChange = ::onValueChange
        )
        Button(
            { generateQRCode(textFieldValue, density) },
            Modifier.height(48.dp),
            enabled = canGenerate
        ) {
            Text(stringResource(R.string.generate_qrcode))
            Icon(
                Icons.Filled.Image,
                stringResource(R.string.image_icon)
            )
        }
        QRCode(qrCodeImage) { qrCodeImage?.let { shareQrCodeImage(context, qrCodeImage) } }
    }
}

@Preview
@Composable
fun GenerateScreenPreview() {
    GenerateScreen(null) { _, _ -> }
}
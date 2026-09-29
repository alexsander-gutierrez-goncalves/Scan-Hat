package br.com.alexsander.leitor.viewmodel

import android.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.alexsander.leitor.data.Code
import br.com.alexsander.leitor.repository.CodeRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun textToQRCode(text: String, density:Float): ImageBitmap {
    val size = (300.dp * density).value.toInt()
    val matrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)

    val bitmap =
        createBitmap(matrix.width, matrix.height).apply {
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val pixelColor = if (matrix[x, y]) Color.BLACK else Color.WHITE
                    setPixel(x, y, pixelColor)
                }
            }
        }
    return bitmap.asImageBitmap()
}

class CodeViewModel(private val codeRepository: CodeRepository) : ViewModel() {
    val codes = codeRepository.codes
    var qrCodeImage by mutableStateOf<ImageBitmap?> (null)
    private set

    fun generateQRCode(text: String, density: Float) {
       qrCodeImage = textToQRCode(text,density)
    }

    fun insert(code: Code) {
        viewModelScope.launch(Dispatchers.IO) {
            codeRepository.insert(code)
        }
    }

    fun delete(code: Code) {
        viewModelScope.launch(Dispatchers.IO) {
            codeRepository.delete(code)
        }
    }
}

class CodeViewModelFactory(private val repository: CodeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CodeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CodeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
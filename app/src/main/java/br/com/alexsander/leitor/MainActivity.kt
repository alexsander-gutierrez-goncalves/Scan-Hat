package br.com.alexsander.leitor

import android.content.ClipData
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.alexsander.leitor.compose.AD
import br.com.alexsander.leitor.compose.BottomBar
import br.com.alexsander.leitor.compose.TopBar
import br.com.alexsander.leitor.data.AppDatabase
import br.com.alexsander.leitor.data.Code
import br.com.alexsander.leitor.repository.CodeRepositoryImpl
import br.com.alexsander.leitor.screens.codesScreen
import br.com.alexsander.leitor.screens.generateScreen
import br.com.alexsander.leitor.screens.homeScreen
import br.com.alexsander.leitor.ui.theme.LeitorTheme
import br.com.alexsander.leitor.viewmodel.CodeViewModel
import br.com.alexsander.leitor.viewmodel.CodeViewModelFactory
import com.google.android.gms.ads.MobileAds
import com.google.mlkit.vision.barcode.BarcodeScanning
import kotlinx.coroutines.launch

enum class ROUTE(@StringRes val title: Int) {
    FIRST(R.string.read_screen),
    SECOND(R.string.codes_screen),
    THIRD(R.string.generate_screen)
}

class MainActivity : ComponentActivity() {
    private val database by lazy { AppDatabase.getInstance(this) }
    private val codeDAO by lazy { database.codeDAO() }
    private val viewModel by viewModels<CodeViewModel>() {
        CodeViewModelFactory(CodeRepositoryImpl(codeDAO))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        installSplashScreen()
        enableEdgeToEdge()

        val barcodeScanner = BarcodeScanning.getClient()

        setContent {
            val navController = rememberNavController()
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val scope = rememberCoroutineScope()
            val snackBarHostState = remember { SnackbarHostState() }
            val context = LocalContext.current
            val clipboard = LocalClipboard.current

            fun showSnackBar(text: String) {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        text, withDismissAction = true
                    )
                }
            }

            fun copy(text: String) {
                val clipData = ClipData.newPlainText("code", text)
                scope.launch { clipboard.setClipEntry(clipData.toClipEntry()) }
                showSnackBar(getString(R.string.copy_action))
            }

            fun delete(code: Code) {
                viewModel.delete(code)
                showSnackBar(getString(R.string.delete_action))
            }

            val currentScreen =
                ROUTE.entries.find { it.name == currentBackStackEntry?.destination?.route.toString() }
            val title = getString(currentScreen?.title ?: R.string.read_screen)

            LeitorTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState, Modifier.semantics {
                            liveRegion = LiveRegionMode.Polite
                            contentDescription = context.getString(R.string.copy_action)
                        })
                    },
                    topBar = { TopBar(title) },
                    bottomBar = { BottomBar(navController, currentBackStackEntry) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        Modifier
                            .padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = ROUTE.FIRST.name,
                            Modifier.weight(1f)
                        ) {
                            homeScreen(barcodeScanner, viewModel) { copy(it) }
                            codesScreen(viewModel, navController, { copy(it) }, { delete(it) })
                            generateScreen(viewModel)
                        }
                        AD()
                    }
                }
            }
        }
    }
}



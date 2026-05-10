package ui.app

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.exitApplication
import ui.viewmodel.ViewModel

/**
 * Llançador Compose per a la versió desktop.
 */
fun main() = application {
    val viewModel = remember { ViewModel() }

    Window(
        onCloseRequest = { exitApplication() },
        title = "Llista Oci"
    ) {
        App(viewModel)
    }
}




package ui.app

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.*
import ui.screens.*
import ui.viewmodel.*

fun main() = application {
    val viewModel = remember { ViewModel() }

    Window(
        onCloseRequest = {
            exitApplication()
        },
        title = "Llista Oci"
    ) {
        MaterialTheme {
            PantallaAplicacio(viewModel)
        }
    }
}

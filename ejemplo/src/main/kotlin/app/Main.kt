package app

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import repository.JsonItemRepository
import ui.AppScreen
import viewmodel.AppViewModel
import java.nio.file.Path

// Punto de entrada de la app Desktop.
// Aqui solo se cablean dependencias y la ventana principal.
fun main() = application {
    // remember evita recrear el ViewModel en cada recomposicion.
    // El ViewModel vive mientras viva la ventana.
    val viewModel = remember {
        AppViewModel(
            repository = JsonItemRepository(
                // Archivo de persistencia en runtime.
                // El repository crea este archivo si no existe.
                filePath = Path.of("data", "items.json")
            )
        )
    }

    Window(
        onCloseRequest = {
            // Cerramos recursos del ViewModel antes de salir.
            viewModel.close()
            exitApplication()
        },
        title = "Ejemplo Compose MVVM"
    ) {
        // Route principal: conecta UI con ViewModel.
        AppScreen(viewModel)
    }
}


package app

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import repository.JsonItemRepository
import ui.AppScreen
import viewmodel.AppViewModel
import java.nio.file.Path

fun main() = application {
    val viewModel = remember {
        AppViewModel(
            repository = JsonItemRepository(
                filePath = Path.of("data", "items.json")
            )
        )
    }

    Window(
        onCloseRequest = {
            viewModel.close()
            exitApplication()
        },
        title = "Ejemplo Compose MVVM"
    ) {
        AppScreen(viewModel)
    }
}


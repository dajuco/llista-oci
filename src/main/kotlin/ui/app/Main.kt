package ui.app

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.nio.file.Path
import repository.*
import ui.*

fun main_() = application {
    val viewModel = remember {
        AppViewModel(
            repository = RepositorioJson()
        )
    }
}
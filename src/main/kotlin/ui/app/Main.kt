package ui.app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import ui.screens.PantallaAplicacio
import ui.viewmodel.ViewModel

/**
 * Arrel Compose compartida per a Android i Desktop.
 *
 * La plataforma es limita a crear la finestra o l'activitat i delega la UI a
 * aquesta funció.
 *
 * @param viewModel ViewModel compartit amb la pantalla principal.
 */
@Composable
fun App(viewModel: ViewModel) {
    MaterialTheme {
        PantallaAplicacio(viewModel)
    }
}

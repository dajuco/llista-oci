package ui.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import ui.viewmodel.ViewModel

/**
 * Punt d'entrada de l'aplicació a Android.
 *
 * Inicialitza les dades JSON a l'emmagatzematge intern i mostra la mateixa UI
 * compartida que utilitza la versió desktop.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidJsonBootstrap.inicialitzar(this)

        setContent {
            val viewModel = remember { ViewModel() }

            App(viewModel)
        }
    }
}



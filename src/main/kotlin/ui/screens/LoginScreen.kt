package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ui.viewmodel.OciState

@Composable
fun PantallaIniciSessio(
    ociState: OciState,
    onUsuariCanvi: (String) -> Unit,
    onContrasenyaCanvi: (String) -> Unit,
    onIniciarSessio: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar sessio")
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = ociState.usuario,
            onValueChange = onUsuariCanvi,
            label = { Text("Usuari") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = ociState.contrasena,
            onValueChange = onContrasenyaCanvi,
            label = { Text("Contrasenya") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onIniciarSessio,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        if (!ociState.errorMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(ociState.errorMessage)
        }
    }
}


package ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import ui.viewmodel.OciState

@Composable
fun PantallaSuperAdmin(
    ociState: OciState,
    onNouNomUsuariCanvi: (String) -> Unit,
    onNovaContrasenyaCanvi: (String) -> Unit,
    onNouDisplayCanvi: (String) -> Unit,
    onNouEsAdminCanvi: (Boolean) -> Unit,
    onCrearUsuario: () -> Unit,
    onRefrescarUsuarios: () -> Unit,
    onNetejarMissatges: () -> Unit,
    onLogout: () -> Unit
) {
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Panell Super Admin")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Benvingut, ${ociState.usuarioLogueado?.display ?: ""}")
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = ociState.nuevoUsername,
            onValueChange = onNouNomUsuariCanvi,
            label = { Text("Nou username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = ociState.nuevoPassword,
            onValueChange = onNovaContrasenyaCanvi,
            label = { Text("Contrasenya") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = ociState.nuevoDisplay,
            onValueChange = onNouDisplayCanvi,
            label = { Text("Display") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = ociState.nuevoEsAdmin, onCheckedChange = onNouEsAdminCanvi)
            Text("Crear com admin")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onCrearUsuario) { Text("Crear usuari") }
            Button(onClick = onRefrescarUsuarios) { Text("Refrescar") }
            Button(onClick = onNetejarMissatges) { Text("Netejar") }
        }

        if (!ociState.infoMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(ociState.infoMessage)
        }

        if (!ociState.errorMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Error: ${ociState.errorMessage}")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Usuaris registrats: ${ociState.usuarios.size}")
        Spacer(modifier = Modifier.height(8.dp))

        ociState.usuarios.forEach { user ->
            Text("- ${user.display} (${user.username}) - ${user.rol}")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onLogout) {
            Text("Tancar sessio")
        }
    }
}


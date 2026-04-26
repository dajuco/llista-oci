package ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import ui.viewmodel.OciState

@Composable
fun PantallaIniciUsuari(
    ociState: OciState,
    onElementObjectiuIdCanvi: (String) -> Unit,
    onAfegirElement: () -> Unit,
    onAvancarEstat: () -> Unit,
    onRetrocedirEstat: () -> Unit,
    onRefrescar: () -> Unit,
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
        Text("Panell Usuari")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Benvingut, ${ociState.usuarioLogueado?.display ?: ""}")
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onRefrescar) { Text("Refrescar") }
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
        Text("Categories")
        ociState.categorias.forEach { categoria ->
            Text("- ${categoria.id} (${categoria.nombre})")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Tots els elements")
        ociState.elementos.forEach { element ->
            Text("- [${element.id}] ${element.titulo} / ${element.categoria.id}")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Els meus elements")
        if (ociState.misElementos.isEmpty()) {
            Text("No tens elements a la teva llista.")
        } else {
            ociState.misElementos.forEach { item ->
                Text("- [${item.elementOciId}] ${item.titulo} - ${item.estado}")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = ociState.elementoObjetivoId,
            onValueChange = onElementObjectiuIdCanvi,
            label = { Text("ID element") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onAfegirElement) { Text("Afegir") }
            Button(onClick = onAvancarEstat) { Text("Avancar") }
            Button(onClick = onRetrocedirEstat) { Text("Retrocedir") }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onLogout) {
            Text("Tancar sessio")
        }
    }
}


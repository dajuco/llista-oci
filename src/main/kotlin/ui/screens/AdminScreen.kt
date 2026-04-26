package ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import ui.viewmodel.OciState

@Composable
fun PantallaAdmin(
    ociState: OciState,
    onNomNovaCategoriaCanvi: (String) -> Unit,
    onCrearCategoria: () -> Unit,
    onNouElementIdCanvi: (String) -> Unit,
    onNouElementTitolCanvi: (String) -> Unit,
    onNouElementDescripcioCanvi: (String) -> Unit,
    onNouElementCategoriaIdCanvi: (String) -> Unit,
    onCrearElemento: () -> Unit,
    onRefrescarCatalogo: () -> Unit,
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
        Text("Panell Admin")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Benvingut, ${ociState.usuarioLogueado?.display ?: ""}")
        Spacer(modifier = Modifier.height(12.dp))

        Text("Crear categoria")
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = ociState.nuevaCategoriaNombre,
            onValueChange = onNomNovaCategoriaCanvi,
            label = { Text("Nom categoria") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onCrearCategoria) { Text("Crear categoria") }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Crear element")
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = ociState.nuevoElementoId,
            onValueChange = onNouElementIdCanvi,
            label = { Text("ID") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = ociState.nuevoElementoTitulo,
            onValueChange = onNouElementTitolCanvi,
            label = { Text("Titol") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = ociState.nuevoElementoDescripcion,
            onValueChange = onNouElementDescripcioCanvi,
            label = { Text("Descripcio") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = ociState.nuevoElementoCategoriaId,
            onValueChange = onNouElementCategoriaIdCanvi,
            label = { Text("ID categoria") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onCrearElemento) { Text("Crear element") }
            Button(onClick = onRefrescarCatalogo) { Text("Refrescar") }
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
        Text("Categories: ${ociState.categorias.size}")
        ociState.categorias.forEach { categoria ->
            Text("- ${categoria.id} (${categoria.nombre})")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Elements: ${ociState.elementos.size}")
        ociState.elementos.forEach { element ->
            Text("- [${element.id}] ${element.titulo} / ${element.categoria.id}")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onLogout) {
            Text("Tancar sessio")
        }
    }
}


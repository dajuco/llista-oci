package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.Item
import model.ItemStatus
import viewmodel.AppViewModel

@Composable
fun AppScreen(viewModel: AppViewModel) {
    val state by viewModel.uiState.collectAsState()

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Compose MVVM - Ejemplo", style = MaterialTheme.typography.h5)
            Text("Flujo: UI -> ViewModel -> Repository -> JSON")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.titleInput,
                    onValueChange = viewModel::onTitleChange,
                    label = { Text("Titulo") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = state.categoryInput,
                    onValueChange = viewModel::onCategoryChange,
                    label = { Text("Categoria") },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = viewModel::addItem) {
                    Text("Agregar")
                }
            }

            state.errorMessage?.let {
                Text(text = it, color = Color(0xFFB00020))
            }

            state.infoMessage?.let {
                Text(text = it, color = Color(0xFF1B5E20))
            }

            if (state.isLoading) {
                CircularProgressIndicator()
            }

            if (!state.isLoading && state.items.isEmpty()) {
                Text("No hay items. Crea el primero con el formulario.")
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.items, key = { it.id }) { item ->
                    ItemRow(
                        item = item,
                        onCycle = { viewModel.cycleStatus(item) },
                        onDelete = { viewModel.deleteItem(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemRow(
    item: Item,
    onCycle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = "${item.id} - ${item.title}")
            Text(text = "Categoria: ${item.category}")
            Text(text = "Estado: ${item.status.readable()}")
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onCycle) { Text("Cambiar estado") }
                Button(onClick = onDelete) { Text("Eliminar") }
            }
        }
    }
}

private fun ItemStatus.readable(): String = when (this) {
    ItemStatus.PENDING -> "PENDIENTE"
    ItemStatus.IN_PROGRESS -> "EN PROCESO"
    ItemStatus.DONE -> "COMPLETADO"
}


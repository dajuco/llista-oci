package viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.Item
import model.ItemStatus
import repository.ItemRepository
import java.io.Closeable
import java.util.UUID

data class AppUiState(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val titleInput: String = "",
    val categoryInput: String = "",
    val errorMessage: String? = null,
    val infoMessage: String? = null
)

class AppViewModel(
    private val repository: ItemRepository
) : Closeable {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(titleInput = value, errorMessage = null, infoMessage = null) }
    }

    fun onCategoryChange(value: String) {
        _uiState.update { it.copy(categoryInput = value, errorMessage = null, infoMessage = null) }
    }

    fun addItem() {
        val snapshot = _uiState.value
        if (snapshot.titleInput.isBlank() || snapshot.categoryInput.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Rellena titulo y categoria") }
            return
        }

        val item = Item(
            id = buildItemId(),
            title = snapshot.titleInput.trim(),
            category = snapshot.categoryInput.trim(),
            status = ItemStatus.PENDING
        )

        scope.launch {
            runCatching {
                repository.add(item)
                repository.loadAll()
            }.onSuccess { items ->
                _uiState.update {
                    it.copy(
                        items = items,
                        titleInput = "",
                        categoryInput = "",
                        errorMessage = null,
                        infoMessage = "Item creado correctamente"
                    )
                }
            }.onFailure { ex ->
                _uiState.update { it.copy(errorMessage = ex.message ?: "No se pudo crear el item") }
            }
        }
    }

    fun cycleStatus(item: Item) {
        val next = when (item.status) {
            ItemStatus.PENDING -> ItemStatus.IN_PROGRESS
            ItemStatus.IN_PROGRESS -> ItemStatus.DONE
            ItemStatus.DONE -> ItemStatus.PENDING
        }

        scope.launch {
            runCatching {
                repository.updateStatus(item.id, next)
                repository.loadAll()
            }.onSuccess { items ->
                _uiState.update { it.copy(items = items, errorMessage = null, infoMessage = "Estado actualizado") }
            }.onFailure { ex ->
                _uiState.update { it.copy(errorMessage = ex.message ?: "No se pudo actualizar") }
            }
        }
    }

    fun deleteItem(item: Item) {
        scope.launch {
            runCatching {
                repository.delete(item.id)
                repository.loadAll()
            }.onSuccess { items ->
                _uiState.update { it.copy(items = items, errorMessage = null, infoMessage = "Item eliminado") }
            }.onFailure { ex ->
                _uiState.update { it.copy(errorMessage = ex.message ?: "No se pudo eliminar") }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, infoMessage = null) }
    }

    private fun loadItems() {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { repository.loadAll() }
                .onSuccess { items ->
                    _uiState.update { it.copy(isLoading = false, items = items) }
                }
                .onFailure { ex ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = ex.message ?: "Error al cargar datos")
                    }
                }
        }
    }

    private fun buildItemId(): String = "IT-${UUID.randomUUID().toString().take(6).uppercase()}"

    override fun close() {
        job.cancel()
        scope.cancel()
    }
}



package repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Item
import model.ItemStatus
import java.nio.file.Files
import java.nio.file.Path

// Implementacion de ItemRepository usando un archivo JSON local.
class JsonItemRepository(
    // Ruta del archivo runtime (ej: data/items.json).
    private val filePath: Path,
    // Recurso semilla copiado en el primer arranque.
    private val seedResourcePath: String = "data/seed_items.json"
) : ItemRepository {

    // Evita corrupciones por escrituras concurrentes.
    private val mutex = Mutex()

    // Configuracion de serializacion JSON.
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    override suspend fun loadAll(): List<Item> = mutex.withLock {
        // Asegura que existe el archivo antes de leer.
        ensureDataFileExists()
        readItems()
    }

    override suspend fun add(item: Item) = mutex.withLock {
        ensureDataFileExists()
        val current = readItems()
        if (current.any { it.id == item.id }) {
            error("Item duplicado: ${item.id}")
        }
        // Reescribe lista completa para mantener el ejemplo simple.
        writeItems(current + item)
    }

    override suspend fun updateStatus(id: String, newStatus: ItemStatus) = mutex.withLock {
        ensureDataFileExists()
        val updated = readItems().map { item ->
            if (item.id == id) item.copy(status = newStatus) else item
        }
        writeItems(updated)
    }

    override suspend fun delete(id: String) = mutex.withLock {
        ensureDataFileExists()
        val filtered = readItems().filterNot { it.id == id }
        writeItems(filtered)
    }

    private fun ensureDataFileExists() {
        if (Files.exists(filePath)) return

        // Crea carpeta padre y copia contenido semilla.
        Files.createDirectories(filePath.parent)
        val seedContent = loadSeedContent()
        Files.writeString(filePath, seedContent)
    }

    private fun loadSeedContent(): String {
        // Si no hay semilla, arranca con lista vacia.
        val input = javaClass.classLoader.getResourceAsStream(seedResourcePath)
            ?: return "[]"
        return input.bufferedReader().use { it.readText() }
    }

    private fun readItems(): List<Item> {
        val content = Files.readString(filePath)
        if (content.isBlank()) return emptyList()
        // Decodifica el JSON en lista tipada de Item.
        return json.decodeFromString(content)
    }

    private fun writeItems(items: List<Item>) {
        // Serializa y persiste en disco.
        Files.writeString(filePath, json.encodeToString(items))
    }
}


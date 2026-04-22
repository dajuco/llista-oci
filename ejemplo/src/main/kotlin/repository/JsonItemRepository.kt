package repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Item
import model.ItemStatus
import java.nio.file.Files
import java.nio.file.Path

class JsonItemRepository(
    private val filePath: Path,
    private val seedResourcePath: String = "data/seed_items.json"
) : ItemRepository {

    private val mutex = Mutex()
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    override suspend fun loadAll(): List<Item> = mutex.withLock {
        ensureDataFileExists()
        readItems()
    }

    override suspend fun add(item: Item) = mutex.withLock {
        ensureDataFileExists()
        val current = readItems()
        if (current.any { it.id == item.id }) {
            error("Item duplicado: ${item.id}")
        }
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

        Files.createDirectories(filePath.parent)
        val seedContent = loadSeedContent()
        Files.writeString(filePath, seedContent)
    }

    private fun loadSeedContent(): String {
        val input = javaClass.classLoader.getResourceAsStream(seedResourcePath)
            ?: return "[]"
        return input.bufferedReader().use { it.readText() }
    }

    private fun readItems(): List<Item> {
        val content = Files.readString(filePath)
        if (content.isBlank()) return emptyList()
        return json.decodeFromString(content)
    }

    private fun writeItems(items: List<Item>) {
        Files.writeString(filePath, json.encodeToString(items))
    }
}


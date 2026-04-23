package repository

import model.Item
import model.ItemStatus

// Contrato de acceso a datos.
// El ViewModel depende de esta interfaz, no de la implementacion concreta.
interface ItemRepository {
    // Devuelve todos los items persistidos.
    suspend fun loadAll(): List<Item>

    // Crea un item nuevo.
    suspend fun add(item: Item)

    // Actualiza el estado de un item por id.
    suspend fun updateStatus(id: String, newStatus: ItemStatus)

    // Elimina un item por id.
    suspend fun delete(id: String)
}


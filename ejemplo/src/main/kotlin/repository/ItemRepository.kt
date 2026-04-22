package repository

import model.Item
import model.ItemStatus

interface ItemRepository {
    suspend fun loadAll(): List<Item>
    suspend fun add(item: Item)
    suspend fun updateStatus(id: String, newStatus: ItemStatus)
    suspend fun delete(id: String)
}


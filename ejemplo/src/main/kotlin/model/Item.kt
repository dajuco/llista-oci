package model

import kotlinx.serialization.Serializable

@Serializable
enum class ItemStatus {
    PENDING,
    IN_PROGRESS,
    DONE
}

@Serializable
data class Item(
    val id: String,
    val title: String,
    val category: String,
    val status: ItemStatus = ItemStatus.PENDING
)


package model

import kotlinx.serialization.Serializable

// Estados simples para mostrar un flujo de trabajo en UI.
// El ViewModel usa este ciclo para cambiar estado con un boton.
@Serializable
enum class ItemStatus {
    PENDING,
    IN_PROGRESS,
    DONE
}

// Entidad persistida en JSON.
// @Serializable permite convertir automaticamente entre Kotlin <-> JSON.
@Serializable
data class Item(
    // Identificador unico usado para actualizar/borrar elementos.
    val id: String,
    // Texto visible en la lista.
    val title: String,
    // Campo libre para clasificar elementos.
    val category: String,
    // Valor por defecto al crear un item nuevo.
    val status: ItemStatus = ItemStatus.PENDING
)


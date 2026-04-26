package models

import kotlinx.serialization.Serializable

@Serializable

enum class Estado(val id: Int, val descripcion: String) {
    PENDENT(1, "Pendent"),
    EN_PROCÉS(2, "En Procés"),
    COMPLETAT(3, "Completat");

    companion object {
        fun obtenirEstat(id: Int): Estado? {
            return values().firstOrNull { it.id == id }
        }

        fun obtenirDescripcio(id: Int): String {
            return obtenirEstat(id)?.descripcion ?: "Desconegut"
        }
    }
}
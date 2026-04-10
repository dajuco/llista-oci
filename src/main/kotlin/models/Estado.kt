package models

enum class Estado(val id: Int, val descripcion: String) {
    PENDENT(1, "Pendent"),
    EN_PROCÉS(2, "En Procés"),
    COMPLETAT(3, "Completat");

    companion object {
        fun getEstado(id: Int): Estado? {
            return values().firstOrNull { it.id == id }
        }

        fun getDescription(id: Int): String {
            return getEstado(id)?.descripcion ?: "Desconegut"
        }
    }
}
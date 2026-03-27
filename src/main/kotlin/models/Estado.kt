package models

enum class Estado(val id: Int, val descripcion: String) {
    PENDIENTE(1, "Pendiente"),
    EN_PROCESO(2, "En Proceso"),
    COMPLETADO(3, "Completado");

    companion object {
        // Devuelve la entrada del enum Estado para un ID dado, o null si no se encuentra
        fun getEstado(id: Int): Estado? {
            return values().firstOrNull { it.id == id }
        }

        // Devuelve la descripción del estado para un ID dado, o "Desconocido" si no se encuentra
        fun getDescription(id: Int): String {
            return getEstado(id)?.descripcion ?: "Desconocido"
        }
    }
}
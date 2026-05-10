package models

import kotlinx.serialization.Serializable

/**
 * Estat possible d'un element associat a un usuari.
 *
 * Cada estat té un identificador numèric i una descripció mostrable a la UI.
 */
@Serializable

enum class Estado(val id: Int, val descripcion: String) {
    PENDENT(1, "Pendent"),
    EN_PROCÉS(2, "En Procés"),
    COMPLETAT(3, "Completat");

    companion object {
        /**
         * Retorna l'estat associat a un identificador numèric.
         *
         * @param id identificador de l'estat.
         * @return l'estat corresponent o `null` si no existeix.
         */
        fun obtenirEstat(id: Int): Estado? {
            return values().firstOrNull { it.id == id }
        }

        /**
         * Retorna la descripció mostrable d'un estat a partir del seu id.
         *
         * @param id identificador de l'estat.
         * @return descripció de l'estat o `Desconegut` si no existeix.
         */
        fun obtenirDescripcio(id: Int): String {
            return obtenirEstat(id)?.descripcion ?: "Desconegut"
        }
    }
}
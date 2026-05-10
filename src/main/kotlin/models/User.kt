package models

import kotlinx.serialization.Serializable

/**
 * Representa qualsevol tipus d'usuari del sistema.
 *
 * La jerarquia es serialitza en JSON i es concreta en els rols
 * d'administració i d'usuari normal.
 */
@Serializable
sealed class User {
    abstract val id: String
    abstract val username: String
    abstract val password: String
    abstract val display: String
}
package models

import kotlinx.serialization.Serializable

/**
 * Usuari amb permisos d'administració del catàleg.
 *
 * Aquest rol pot crear categories i elements.
 */
@Serializable
class UserAdmin(
    override val username: String,
    override val password: String,
    override val display: String,
    override val id: String = username
) : User()

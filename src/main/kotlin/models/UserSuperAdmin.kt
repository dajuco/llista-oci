package models

import kotlinx.serialization.Serializable

/**
 * Usuari amb permisos totals sobre la gestió d'usuaris.
 *
 * Pot crear comptes administradors o normals i revisar la llista completa
 * d'usuaris registrats.
 */
@Serializable
class UserSuperAdmin(
    override val username: String,
    override val password: String,
    override val display: String,
    override val id: String = username
) : User()

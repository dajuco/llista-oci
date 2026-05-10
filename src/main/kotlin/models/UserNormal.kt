package models

import kotlinx.serialization.Serializable

/**
 * Usuari estàndard de l'aplicació.
 *
 * @property username nom d'usuari.
 * @property password contrasenya.
 * @property display nom visible.
 * @property id identificador intern, alineat amb el nom d'usuari.
 * @property elementsUser elements personals de l'usuari.
 */
@Serializable
class UserNormal(
    override val username: String,
    override val password: String,
    override val display: String,
    override val id: String = username,
    val elementsUser: MutableList<ElementUsuari> = mutableListOf()
) : User() {


    /**
     * Afegeix un element a la llista personal de l'usuari.
     *
     * @param element element d'oci associat amb el seu estat.
     */
    fun afegirElement(element: ElementUsuari) {
        elementsUser.add(element)
    }
}
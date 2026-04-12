package models

import kotlinx.serialization.Serializable

@Serializable
class UserNormal(
    override val username: String,
    override val password: String,
    override val display: String,
    override val id: String = username,
    val elementsUser: MutableList<ElementUsuari> = mutableListOf()
) : User() {


    fun crearElemento(elemento: ElementUsuari) {
        elementsUser.add(elemento)
    }
}
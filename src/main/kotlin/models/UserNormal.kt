package models

class UserNormal(
    username: String,
    password: String,
    display: String,
    val elementsUser: MutableList<ElementUsuari> = mutableListOf()
) : User(username, password, display) {


    fun crearElemento(elemento: ElementUsuari) {
        elementsUser.add(elemento)
    }
}
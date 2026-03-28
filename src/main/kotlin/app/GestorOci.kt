package app

import models.*

class GestorOci {

    val elements: MutableList<ElementOci> = mutableListOf()

    val categories: MutableList<Categoria> = mutableListOf()

    val users: MutableList<User> = mutableListOf()

    init {
        val root = UserSuperAdmin("super", "1234", "Super User")
        users.add(root)
    }

    fun crearElemento(elemento: ElementOci) {
        elements.add(elemento)
        println("Element creat: $elemento")
    }

    fun crearCategoria(categoria: Categoria) {
        categories.add(categoria)
        println("Categoria creada: $categoria")
    }

    fun crearUser(username: String, password: String, display: String, admin: Boolean) {
        if (admin) {
            users.add(UserAdmin(username, password, display))
        }
        else {
            users.add(UserNormal(username, password, display))
        }
    }


}
package app
import models.*

class GestorOci {

    val elements: MutableList<ElementOci> = mutableListOf()

    val categories: MutableList<Categoria> = mutableListOf()

    val users: MutableList<User> = mutableListOf()

    val root = UserSuperAdmin("admin", "1234", "Super User")

    fun crearElemento(elemento: ElementOci) {
        elements.add(elemento)
        println("Element creat: $elemento")
    }

    fun crearCategoria(categoria: Categoria) {
        categories.add(categoria)
        println("Categoria creada: $categoria")
    }

    fun crearUser(user: User) {
        users.add(user)
        println("User creat: $user")
    }


}

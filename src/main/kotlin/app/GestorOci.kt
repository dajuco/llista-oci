package app

import models.*
import exceptions.*

class GestorOci {

    val elements: MutableList<ElementOci> = mutableListOf()

    val categories: MutableList<Categoria> = mutableListOf()

    val users: MutableList<User> = mutableListOf()

    init {
        val root = UserSuperAdmin("super", "1234", "Super User")
        users.add(root)
    }

    private fun validarElementOci(elemento: ElementOci) {
        if (elemento.id.isBlank()) {
            throw ValidacioException("l'id de l'element no pot estar buit.")
        }
        if (elemento.titulo.isBlank()) {
            throw ValidacioException("El títol no pot estar buit.")
        }
        
        val idRegex = "^[a-zA-Z0-9_-]+\$".toRegex()
        if (!idRegex.matches(elemento.id)) {
            throw ValidacioException("l'id conte caràcters no vàlids. Només s'accepten lletres, números o guions.")
        }
    }

    fun crearElemento(elemento: ElementOci) {
        validarElementOci(elemento)
        
        if (elements.any { it.id == elemento.id }) {
            throw ElementDuplicatException("Ja existeix un element amb l'ID: ${elemento.id}")
        }
        
        elements.add(elemento)
        println("Element creat: $elemento")
    }

    fun crearCategoria(categoria: Categoria) {
        if (categoria.nombre.isBlank()) {
            throw ValidacioException("El nom de la categoria no pot estar buit.")
        }
        if (categories.any { it.nombre.equals(categoria.nombre, ignoreCase = true) }) {
            throw ElementDuplicatException("La categoria '${categoria.nombre}' ja existeix.")
        }
        
        categories.add(categoria)
        println("Categoria creada: $categoria")
    }

    fun crearUser(username: String, password: String, display: String, admin: Boolean) {
        if (username.isBlank() || password.isBlank()) {
            throw ValidacioException("L'usuari i la contrasenya no poden estar buits.")
        }
        if (users.any { it.username == username }) {
            throw ElementDuplicatException("L'usuari '$username' ja existeix.")
        }
        
        if (admin) {
            users.add(UserAdmin(username, password, display))
        } else {
            users.add(UserNormal(username, password, display))
        }
    }
}
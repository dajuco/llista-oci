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
                 throw TextBuitException("l'id de l'element no pot estar buit.")
            }
            if (elemento.titulo.isBlank()) {
                 throw TextBuitException("El títol no pot estar buit.")
            }

            val idRegex = "^[a-zA-Z0-9_-]+\$".toRegex()
            if (!idRegex.matches(elemento.id)) {
                throw ValidacioException("l'id conte caràcters no vàlids. Només s'accepten lletres, números o guions.")
            }
    }

    fun crearElemento(elemento: ElementOci) {
        try {
            validarElementOci(elemento)

            if (elements.any { it.id == elemento.id }) {
                throw ElementDuplicatException("Ja existeix un element amb l'ID: ${elemento.id}")
            }

             elements.add(elemento)
            println("Element creat: $elemento")
         } catch (e:ElementDuplicatException) {
            println("Error: ${e.message}")
        } catch (e: TextBuitException) {
            println("Error: ${e.message}")
        } catch (e: ValidacioException) {
            println("Error: ${e.message}")
        }
    }

    fun crearCategoria(categoria: Categoria) {
        try {
            if (categoria.nombre.isBlank()) {
                throw TextBuitException("El nom de la categoria no pot estar buit.")
            }
            if (categories.any { it.nombre.equals(categoria.nombre, ignoreCase = true) }) {
                throw ElementDuplicatException("La categoria '${categoria.nombre}' ja existeix.")
            }

            categories.add(categoria)
            println("Categoria creada: $categoria")
         } catch (e: TextBuitException) {
            println("Error: ${e.message}")
         } catch (e:ElementDuplicatException) {
            println("Error: ${e.message}")
        }
    }

    fun crearUser(username: String, password: String, display: String, admin: Boolean) {
        try {
            if (username.isBlank() || password.isBlank()) {
                throw TextBuitException("L'usuari i la contrasenya no poden estar buits.")
            }
            if (users.any { it.username == username }) {
                throw ElementDuplicatException("L'usuari '$username' ja existeix.")
            }

             if (admin) {
                users.add(UserAdmin(username, password, display))
                println("Usuari administrador creat correctament.")
             } else {
                users.add(UserNormal(username, password, display))
                println("Usuari creat correctament.")
             }

        } catch (e: TextBuitException) {
            println("Error: ${e.message}")
        } catch (e:ElementDuplicatException) {
            println("Error: ${e.message}")
        }
        
    }
}
package app

import models.*
import exceptions.*

class GestorOci {

    val elements: MutableList<ElementOci> = mutableListOf()

    val categories: MutableList<Categoria> = mutableListOf()

    val users: MutableList<User> = mutableListOf()

    init {
        val root = UserSuperAdmin("super", "1234", "Super Usuari")
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
         } catch (e: Exception) {
             println(e.message)
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
            println("Categoria creada correctament.")
         } catch (e: TextBuitException) {
            println("Error: ${e.message}")
         } catch (e:ElementDuplicatException) {
            println("Error: ${e.message}")
         } catch (e: Exception) {
             println(e.message)
         }
    }

    fun crearUser(username: String, password: String, display: String, admin: Boolean) {
        try {
            if (username.isBlank() || password.isBlank() || display.isBlank()) {
                throw TextBuitException("L'usuari, la contrasenya i el display no poden estar buits.")
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
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun mostrarElementosFormateados() {
        if (elements.isEmpty()) {
            println("No hi ha elements registrats.")
            return
        }
        println("ID\t\tTítol\t\tDescripció\t\tCategoria")
        println("-".repeat(60))
        elements.forEach {
            println("${it.id.padEnd(10)}\t${it.titulo.padEnd(20)}\t${it.descripcion.take(20).padEnd(20)}\t${it.categoria.nombre}")
        }
    }

    fun mostrarCategoriasFormateadas() {
        if (categories.isEmpty()) {
            println("No hi ha categories registrades.")
            return
        }
        println("Nom de la Categoria")
        println("-".repeat(20))
        categories.forEach {
            println(it.nombre)
        }
    }

    fun mostrarUsuariosFormateados() {
        if (users.isEmpty()) {
            println("No hi ha usuaris registrats.")
            return
        }
        println("Display\t\tUsername\t\tRol")
        println("-".repeat(40))
        users.forEach {
            val rol = when {
                it.username == "super" -> "Super"
                it is UserAdmin -> "Admin"
                else -> "User"
            }
            println("${it.display.padEnd(15)}\t${it.username.padEnd(15)}\t$rol")
        }
    }

    fun mostrarMisElementosFormateados(user: UserNormal) {
        if (user.elementsUser.isEmpty()) {
            println("No tens elements a la teva llista.")
            return
        }
        println("ID\t\tTítol\t\tEstat")
        println("-".repeat(40))
        user.elementsUser.forEach { elemUser ->
            val elemOriginal = elements.find { it.id == elemUser.elementOciId }
            if (elemOriginal != null) {
                println("${elemUser.elementOciId.padEnd(10)}\t${elemOriginal.titulo.padEnd(20)}\t${elemUser.estado.descripcion}")
            }
        }
    }
}
package app

import models.*
import exceptions.*
import repository.*

class GestorOci {

    private val repositorioElemento = GestorRepositorio.repositorioElemento
    private val repositorioCategoria = GestorRepositorio.repositorioCategoria
    private val repositorioUsuario = GestorRepositorio.repositorioUsuario

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

            if (repositorioElemento.encontrarPorId(elemento.id) != null) {
                throw ElementDuplicatException("Ja existeix un element amb l'ID: ${elemento.id}")
            }

            repositorioElemento.guardar(elemento)
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
            if (repositorioCategoria.encontrarPorId(categoria.id) != null) {
                throw ElementDuplicatException("La categoria amb nom '${categoria.nombre}' ja existeix.")
            }

            repositorioCategoria.guardar(categoria)

            println("Categoria creada correctament.")
         } catch (e: TextBuitException) {
            println("Error: ${e.message}")
         } catch (e:ElementDuplicatException) {
            println("Error: ${e.message}")
         } catch (e: Exception) {
             println(e.message)
         }
    }

    private fun validarCamposUser(username: String, password: String, display: String) {
        if (username.isBlank()) {
            throw TextBuitException("El nom d'usuari no pot estar buit.")
        }
        if (password.isBlank()) {
            throw TextBuitException("La contrasenya no pot estar buida.")
        }
        if (display.isBlank()) {
            throw TextBuitException("El nom a mostrar (display) no pot estar buit.")
        }
    }

    fun crearUser(username: String, password: String, display: String, admin: Boolean) {
        try {
            validarCamposUser(username, password, display)
            if (repositorioUsuario.encontrarPorUser(username) != null) {
                throw ElementDuplicatException("L'usuari '$username' ja existeix.")
            }

             if (admin) {
                 repositorioUsuario.guardar(UserAdmin(username, password, display))
                println("Usuari administrador creat correctament.")
             } else {
                 repositorioUsuario.guardar(UserNormal(username, password, display))
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

    fun mostrarElementos() {
        if (repositorioElemento.encontrarTodos().isEmpty()) {
            println("No hi ha elements registrats.")
            return
        }

        repositorioElemento.encontrarTodos().forEach {
            println(it)
        }
    }

    fun mostrarCategorias() {
        if (repositorioCategoria.encontrarTodos().isEmpty()) {
            println("No hi ha categories registrades.")
            return
        }
        repositorioCategoria.encontrarTodos().forEach {
            println(it)
        }
    }

    fun hayCategoriasDisponibles(): Boolean {
        return repositorioCategoria.encontrarTodos().isNotEmpty()
    }

    fun obtenerCategorias(): List<Categoria> {
        return repositorioCategoria.encontrarTodos()
    }

    fun hayElementosDisponibles(): Boolean {
        return repositorioElemento.encontrarTodos().isNotEmpty()
    }

    fun mostrarUsuarios() {
        if (repositorioUsuario.encontrarTodos().isEmpty()) {
            println("No hi ha usuaris registrats.")
            return
        }
        repositorioUsuario.encontrarTodos().forEach {
            println("${it.display} (${it.username}) - ${if (it is UserSuperAdmin) "Super Administrador" else if (it is UserAdmin) "Administrador" else "Usuari Normal"}")
        }
    }

    fun mostrarMisElementos(user: UserNormal) {
        val userActual = repositorioUsuario.encontrarPorUser(user.username) as? UserNormal
        if (userActual == null || userActual.elementsUser.isEmpty()) {
            println("No tens elements a la teva llista.")
            return
        }
        userActual.elementsUser.forEach { elemUser ->
            val elemOriginal = repositorioElemento.encontrarPorId(elemUser.elementOciId)
            if (elemOriginal != null) {
                println("[${elemUser.elementOciId}] ${elemOriginal.titulo} - ${elemUser.estado.descripcion}")
            } else {
                println("[${elemUser.elementOciId}] (element no disponible) - ${elemUser.estado.descripcion}")
            }
        }

        user.elementsUser.clear()
        user.elementsUser.addAll(userActual.elementsUser)
    }

    fun añadirElementoAUsuario(user: UserNormal, idElemento: String): String {
        if (idElemento.isBlank())
            throw TextBuitException("L'ID de l'element no pot estar buit.")


        val elementoOriginal = repositorioElemento.encontrarPorId(idElemento)
        if (elementoOriginal == null)
            throw ElementNoTrobatException("No s'ha trobat cap element amb l'ID '$idElemento'.")


        val userActual = repositorioUsuario.encontrarPorUser(user.username) as? UserNormal

        if (userActual == null)
            throw ElementNoTrobatException("No s'ha trobat l'usuari '${user.username}'.")


        if (userActual.elementsUser.any { it.elementOciId == idElemento })
            throw ElementDuplicatException("Aquest element ja està a la teva llista.")


        userActual.crearElemento(ElementUsuari(idElemento, estado = Estado.PENDENT))
        repositorioUsuario.actualizar(userActual)

        user.elementsUser.clear()
        user.elementsUser.addAll(userActual.elementsUser)

        return elementoOriginal.titulo
    }

    fun usuarioTieneElementos(user: UserNormal): Boolean {
        val userActual = repositorioUsuario.encontrarPorUser(user.username) as? UserNormal
        return userActual?.elementsUser?.isNotEmpty() == true
    }

    fun avanzarEstadoElementoUsuario(user: UserNormal, idElemento: String): String {
        if (idElemento.isBlank()) {
            throw TextBuitException("L'ID de l'element no pot estar buit.")
        }

        val userActual = repositorioUsuario.encontrarPorUser(user.username) as? UserNormal
        if (userActual == null) {
            throw ElementNoTrobatException("No s'ha trobat l'usuari '${user.username}'.")
        }

        val elemento = userActual.elementsUser.find { it.elementOciId == idElemento }
        if (elemento == null) {
            throw ElementNoTrobatException("L'element amb ID '$idElemento' no es troba a la teva llista.")
        }

        elemento.AvanzarEstado()
        repositorioUsuario.actualizar(userActual)

        user.elementsUser.clear()
        user.elementsUser.addAll(userActual.elementsUser)

        return elemento.estado.descripcion
    }

    fun retrocederEstadoElementoUsuario(user: UserNormal, idElemento: String): String {
        if (idElemento.isBlank()) {
            throw TextBuitException("L'ID de l'element no pot estar buit.")
        }

        val userActual = repositorioUsuario.encontrarPorUser(user.username) as? UserNormal
        if (userActual == null) {
            throw ElementNoTrobatException("No s'ha trobat l'usuari '${user.username}'.")
        }

        val elemento = userActual.elementsUser.find { it.elementOciId == idElemento }
        if (elemento == null) {
            throw ElementNoTrobatException("L'element amb ID '$idElemento' no es troba a la teva llista.")
        }

        elemento.RetrocederEstado()
        repositorioUsuario.actualizar(userActual)

        user.elementsUser.clear()
        user.elementsUser.addAll(userActual.elementsUser)

        return elemento.estado.descripcion
    }
}
package app

import models.*
import exceptions.*
import repository.*

/**
 * Coordinador de la lògica de negoci de l'aplicació.
 *
 * Valida les dades, aplica regles de negoci i delega la persistència al
 * repositori JSON compartit entre Desktop i Android.
 */
class GestorOci {

    private val repositorioElemento = GestorRepositorio.repositorioElemento
    private val repositorioCategoria = GestorRepositorio.repositorioCategoria
    private val repositorioUsuario = GestorRepositorio.repositorioUsuario

    private fun validarElementLleure(element: ElementOci) {
            if (element.id.isBlank()) {
                 throw TextBuitException("l'id de l'element no pot estar buit.")
            }
            if (element.titulo.isBlank()) {
                 throw TextBuitException("El títol no pot estar buit.")
            }

            val idRegex = "^[a-zA-Z0-9_-]+\$".toRegex()
            if (!idRegex.matches(element.id)) {
                throw ValidacioException("l'id conte caràcters no vàlids. Només s'accepten lletres, números o guions.")
            }
    }

    /**
     * Desa un nou element d'oci al repositori.
     *
     * @param element element a crear.
     */
    fun crearElement(element: ElementOci) {
        try {
            validarElementLleure(element)

            if (repositorioElemento.trobarPerId(element.id) != null) {
                throw ElementDuplicatException("Ja existeix un element amb l'ID: ${element.id}")
            }

            repositorioElemento.desar(element)
            println("Element creat: $element")
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

    /**
     * Desa una nova categoria al repositori.
     *
     * @param categoria categoria a crear.
     */
    fun crearCategoria(categoria: Categoria) {
        try {
            if (categoria.nombre.isBlank()) {
                throw TextBuitException("El nom de la categoria no pot estar buit.")
            }
            if (repositorioCategoria.trobarPerId(categoria.id) != null) {
                throw ElementDuplicatException("La categoria amb nom '${categoria.nombre}' ja existeix.")
            }

            repositorioCategoria.desar(categoria)

            println("Categoria creada correctament.")
         } catch (e: TextBuitException) {
            println("Error: ${e.message}")
         } catch (e:ElementDuplicatException) {
            println("Error: ${e.message}")
         } catch (e: Exception) {
             println(e.message)
         }
    }

    private fun validarCampsUsuari(username: String, password: String, display: String) {
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

    /**
     * Crea un usuari normal o administrador segons el valor d'`admin`.
     *
     * @param username nom d'usuari.
     * @param password contrasenya de l'usuari.
     * @param display nom visible a la interfície.
     * @param admin indica si cal crear un usuari administrador.
     */
    fun crearUsuari(username: String, password: String, display: String, admin: Boolean) {
        try {
            validarCampsUsuari(username, password, display)
            if (repositorioUsuario.trobarPerUsuari(username) != null) {
                throw ElementDuplicatException("L'usuari '$username' ja existeix.")
            }

             if (admin) {
                  repositorioUsuario.desar(UserAdmin(username, password, display))
                println("Usuari administrador creat correctament.")
             } else {
                  repositorioUsuario.desar(UserNormal(username, password, display))
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

    /**
     * Escriu per consola tots els elements disponibles.
     */
    fun mostrarElements() {
        if (repositorioElemento.trobarTots().isEmpty()) {
            println("No hi ha elements registrats.")
            return
        }

        repositorioElemento.trobarTots().forEach {
            println(it)
        }
    }

    /**
     * Escriu per consola totes les categories registrades.
     */
    fun mostrarCategories() {
        if (repositorioCategoria.trobarTots().isEmpty()) {
            println("No hi ha categories registrades.")
            return
        }
        repositorioCategoria.trobarTots().forEach {
            println(it)
        }
    }

    /**
     * Indica si hi ha categories disponibles.
     *
     * @return `true` si existeix almenys una categoria.
     */
    fun hiHaCategoriesDisponibles(): Boolean {
        return repositorioCategoria.trobarTots().isNotEmpty()
    }

    /**
     * Obté totes les categories disponibles.
     *
     * @return llista de categories.
     */
    fun obtenirCategories(): List<Categoria> {
        return repositorioCategoria.trobarTots()
    }

    /**
     * Indica si hi ha elements d'oci registrats.
     *
     * @return `true` si el catàleg no està buit.
     */
    fun hiHaElementsDisponibles(): Boolean {
        return repositorioElemento.trobarTots().isNotEmpty()
    }

    /**
     * Escriu per consola tots els usuaris registrats.
     */
    fun mostrarUsuaris() {
        if (repositorioUsuario.trobarTots().isEmpty()) {
            println("No hi ha usuaris registrats.")
            return
        }
        repositorioUsuario.trobarTots().forEach {
            println("${it.display} (${it.username}) - ${if (it is UserSuperAdmin) "Super Administrador" else if (it is UserAdmin) "Administrador" else "Usuari Normal"}")
        }
    }

    /**
     * Escriu per consola la llista personal d'elements d'un usuari normal.
     *
     * @param user usuari autenticat.
     */
    fun mostrarMeusElements(user: UserNormal) {
        val userActual = repositorioUsuario.trobarPerUsuari(user.username) as? UserNormal
        if (userActual == null || userActual.elementsUser.isEmpty()) {
            println("No tens elements a la teva llista.")
            return
        }
        userActual.elementsUser.forEach { elemUser ->
            val elemOriginal = repositorioElemento.trobarPerId(elemUser.elementOciId)
            if (elemOriginal != null) {
                println("[${elemUser.elementOciId}] ${elemOriginal.titulo} - ${elemUser.estado.descripcion}")
            } else {
                println("[${elemUser.elementOciId}] (element no disponible) - ${elemUser.estado.descripcion}")
            }
        }

        user.elementsUser.clear()
        user.elementsUser.addAll(userActual.elementsUser)
    }

    /**
     * Afegeix un element a la llista personal de l'usuari.
     *
     * @param user usuari autenticat.
     * @param idElement identificador de l'element a afegir.
     * @return títol de l'element afegit.
     */
    fun afegirElementAUsuari(user: UserNormal, idElement: String): String {
        if (idElement.isBlank())
            throw TextBuitException("L'ID de l'element no pot estar buit.")


        val elementoOriginal = repositorioElemento.trobarPerId(idElement)
        if (elementoOriginal == null)
            throw ElementNoTrobatException("No s'ha trobat cap element amb l'ID '$idElement'.")


        val userActual = repositorioUsuario.trobarPerUsuari(user.username) as? UserNormal

        if (userActual == null)
            throw ElementNoTrobatException("No s'ha trobat l'usuari '${user.username}'.")


        if (userActual.elementsUser.any { it.elementOciId == idElement })
            throw ElementDuplicatException("Aquest element ja està a la teva llista.")


        userActual.afegirElement(ElementUsuari(idElement, estado = Estado.PENDENT))
        repositorioUsuario.actualitzar(userActual)

        user.elementsUser.clear()
        user.elementsUser.addAll(userActual.elementsUser)

        return elementoOriginal.titulo
    }

    /**
     * Indica si l'usuari té elements a la seva llista.
     *
     * @param user usuari autenticat.
     * @return `true` si hi ha elements a la llista personal.
     */
    fun usuariTeElements(user: UserNormal): Boolean {
        val userActual = repositorioUsuario.trobarPerUsuari(user.username) as? UserNormal
        return userActual?.elementsUser?.isNotEmpty() == true
    }

    /**
     * Avança l'estat d'un element personalitzat de l'usuari.
     *
     * @param user usuari autenticat.
     * @param idElement identificador de l'element a modificar.
     * @return descripció textual del nou estat.
     */
    fun avancarEstatElementUsuari(user: UserNormal, idElement: String): String {
        if (idElement.isBlank()) {
            throw TextBuitException("L'ID de l'element no pot estar buit.")
        }

        val userActual = repositorioUsuario.trobarPerUsuari(user.username) as? UserNormal
        if (userActual == null) {
            throw ElementNoTrobatException("No s'ha trobat l'usuari '${user.username}'.")
        }

        val elemento = userActual.elementsUser.find { it.elementOciId == idElement }
        if (elemento == null) {
            throw ElementNoTrobatException("L'element amb ID '$idElement' no es troba a la teva llista.")
        }

        elemento.avançarEstat()
        repositorioUsuario.actualitzar(userActual)

        user.elementsUser.clear()
        user.elementsUser.addAll(userActual.elementsUser)

        return elemento.estado.descripcion
    }

    /**
     * Retrocedeix l'estat d'un element personalitzat de l'usuari.
     *
     * @param user usuari autenticat.
     * @param idElement identificador de l'element a modificar.
     * @return descripció textual del nou estat.
     */
    fun retrocedirEstatElementUsuari(user: UserNormal, idElement: String): String {
        if (idElement.isBlank()) {
            throw TextBuitException("L'ID de l'element no pot estar buit.")
        }

        val userActual = repositorioUsuario.trobarPerUsuari(user.username) as? UserNormal
        if (userActual == null) {
            throw ElementNoTrobatException("No s'ha trobat l'usuari '${user.username}'.")
        }

        val elemento = userActual.elementsUser.find { it.elementOciId == idElement }
        if (elemento == null) {
            throw ElementNoTrobatException("L'element amb ID '$idElement' no es troba a la teva llista.")
        }

        elemento.retrocedirEstat()
        repositorioUsuario.actualitzar(userActual)

        user.elementsUser.clear()
        user.elementsUser.addAll(userActual.elementsUser)

        return elemento.estado.descripcion
    }
}
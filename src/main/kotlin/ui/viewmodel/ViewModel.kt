package ui.viewmodel

import app.GestorOci
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import exceptions.*
import models.*
import repository.*

/**
 * Resum simplificat d'un usuari per mostrar-lo a la interfície.
 *
 * @property username nom d'usuari únic.
 * @property display nom visible a la UI.
 * @property rol descripció textual del rol.
 */
data class UserSummary(
    val username: String,
    val display: String,
    val rol: String
)

/**
 * Element de la llista personal d'un usuari amb el seu estat actual.
 *
 * @property elementOciId identificador de l'element d'oci.
 * @property titulo títol visible a la UI.
 * @property estado estat textual actual.
 */
data class UserElementItem(
    val elementOciId: String,
    val titulo: String,
    val estado: String
)

/**
 * Estat complet de la pantalla compartit entre Desktop i Android.
 *
 * Agrupa els camps de formulari, els missatges i les col·leccions mostrades
 * per cada rol d'usuari.
 */
data class OciState(
	val usuario: String = "",
	val contrasena: String = "",
	val usuarioLogueado: User? = null,
	val errorMessage: String? = null,
	val infoMessage: String? = null,
	val usuarios: List<UserSummary> = emptyList(),
	val categorias: List<Categoria> = emptyList(),
	val elementos: List<ElementOci> = emptyList(),
	val misElementos: List<UserElementItem> = emptyList(),
	val nuevoUsername: String = "",
	val nuevoPassword: String = "",
	val nuevoDisplay: String = "",
	val nuevoEsAdmin: Boolean = false,
	val nuevaCategoriaNombre: String = "",
	val nuevoElementoId: String = "",
	val nuevoElementoTitulo: String = "",
	val nuevoElementoDescripcion: String = "",
	val nuevoElementoCategoriaId: String = "",
	val elementoObjetivoId: String = ""
)

/**
 * ViewModel principal de l'aplicació.
 *
 * Centralitza l'estat de la UI i encapsula les crides al gestor de negoci
 * perquè la pantalla només hagi d'emetre intents d'usuari.
 */
class ViewModel {
	private val gestorOci = GestorOci()

	var ociState by mutableStateOf(OciState())
		private set

	/**
	 * Actualitza el camp de nom d'usuari del formulari d'inici de sessió.
	 *
	 * @param usuari text introduït per l'usuari.
	 */
	fun actualitzarUsuari(usuari: String) {
		ociState = ociState.copy(usuario = usuari, errorMessage = null, infoMessage = null)
	}

	/**
	 * Actualitza el camp de contrasenya del formulari d'inici de sessió.
	 *
	 * @param contrasenya text introduït per l'usuari.
	 */
	fun actualitzarContrasenya(contrasenya: String) {
		ociState = ociState.copy(contrasena = contrasenya, errorMessage = null, infoMessage = null)
	}

	/**
	 * Intenta iniciar sessió amb les credencials actuals.
	 *
	 * Si l'autenticació és correcta, carrega les dades del rol corresponent.
	 */
	fun iniciarSessio() {
		val username = ociState.usuario.trim()
		val password = ociState.contrasena.trim()

		if (username.isBlank() || password.isBlank()) {
			ociState = ociState.copy(
				usuarioLogueado = null,
				errorMessage = "L'usuari i la contrasenya no poden estar buits.",
				infoMessage = null
			)
			return
		}

		val usuario = GestorRepositorio.repositorioUsuario
			.trobarTots()
			.find { it.username == username && it.password == password }

		ociState = if (usuario != null) {
			ociState.copy(
				usuarioLogueado = usuario,
				errorMessage = null,
				infoMessage = "Sessio iniciada correctament."
			)
		} else {
			ociState.copy(
				usuarioLogueado = null,
				errorMessage = "Credencials incorrectes.",
				infoMessage = null
			)
		}

		refrescarDadesRolActual()
	}

	private fun rolDeUsuari(user: User): String {
		return when (user) {
			is UserSuperAdmin -> "Super Admin"
			is UserAdmin -> "Admin"
			is UserNormal -> "Usuari"
		}
	}

	/**
	 * Recarrega les dades visibles segons el rol de l'usuari autenticat.
	 */
	fun refrescarDadesRolActual() {
		val user = ociState.usuarioLogueado ?: return

		when (user) {
			is UserSuperAdmin -> carregarUsuaris()
			is UserAdmin -> carregarCataleg()
			is UserNormal -> {
				carregarCataleg()
				carregarMeusElements()
			}
		}
	}

	/**
	 * Carrega el llistat d'usuaris per al panell de superadministració.
	 */
	fun carregarUsuaris() {
		val usuaris = GestorRepositorio.repositorioUsuario.trobarTots().map {
			UserSummary(it.username, it.display, rolDeUsuari(it))
		}
		ociState = ociState.copy(usuarios = usuaris)
	}

	/**
	 * Carrega el catàleg de categories i elements per als rols que el necessiten.
	 */
	fun carregarCataleg() {
		ociState = ociState.copy(
			categorias = GestorRepositorio.repositorioCategoria.trobarTots(),
			elementos = GestorRepositorio.repositorioElemento.trobarTots()
		)
	}

	/**
	 * Carrega la llista personal d'elements de l'usuari autenticat.
	 */
	fun carregarMeusElements() {
		val user = ociState.usuarioLogueado as? UserNormal ?: return
		val userActual = GestorRepositorio.repositorioUsuario.trobarPerUsuari(user.username) as? UserNormal ?: return

		val items = userActual.elementsUser.map { item ->
			val titulo = GestorRepositorio.repositorioElemento
				.trobarPerId(item.elementOciId)
				?.titulo ?: "(element no disponible)"

			UserElementItem(
				elementOciId = item.elementOciId,
				titulo = titulo,
				estado = item.estado.descripcion
			)
		}

		ociState = ociState.copy(
			usuarioLogueado = userActual,
			misElementos = items
		)
	}

	/**
	 * Actualitza el nom d'usuari del formulari de creació d'usuaris.
	 *
	 * @param value nou valor del camp.
	 */
	fun actualitzarNouNomUsuari(value: String) {
		ociState = ociState.copy(nuevoUsername = value, errorMessage = null, infoMessage = null)
	}

	/**
	 * Actualitza la contrasenya del formulari de creació d'usuaris.
	 *
	 * @param value nou valor del camp.
	 */
	fun actualitzarNovaContrasenya(value: String) {
		ociState = ociState.copy(nuevoPassword = value, errorMessage = null, infoMessage = null)
	}

	/**
	 * Actualitza el nom visible del formulari de creació d'usuaris.
	 *
	 * @param value nou valor del camp.
	 */
	fun actualitzarNouDisplay(value: String) {
		ociState = ociState.copy(nuevoDisplay = value, errorMessage = null, infoMessage = null)
	}

	/**
	 * Marca si el nou usuari s'ha de crear com a administrador.
	 *
	 * @param value `true` si s'ha de crear com a admin.
	 */
	fun actualitzarNouEsAdmin(value: Boolean) {
		ociState = ociState.copy(nuevoEsAdmin = value)
	}

	/**
	 * Crea un usuari a partir de les dades del formulari.
	 */
	fun crearUsuari() {
		val username = ociState.nuevoUsername.trim()
		val password = ociState.nuevoPassword.trim()
		val display = ociState.nuevoDisplay.trim()

		if (username.isBlank() || password.isBlank() || display.isBlank()) {
			ociState = ociState.copy(errorMessage = "Tots els camps d'usuari son obligatoris.", infoMessage = null)
			return
		}

		if (GestorRepositorio.repositorioUsuario.trobarPerUsuari(username) != null) {
			ociState = ociState.copy(errorMessage = "L'usuari '$username' ja existeix.", infoMessage = null)
			return
		}

		gestorOci.crearUsuari(username, password, display, ociState.nuevoEsAdmin)
		ociState = ociState.copy(
			nuevoUsername = "",
			nuevoPassword = "",
			nuevoDisplay = "",
			nuevoEsAdmin = false,
			errorMessage = null,
			infoMessage = "Usuari creat correctament."
		)
		carregarUsuaris()
	}

	/**
	 * Actualitza el nom de la nova categoria.
	 *
	 * @param value valor introduït a la UI.
	 */
	fun actualitzarNomNovaCategoria(value: String) {
		ociState = ociState.copy(nuevaCategoriaNombre = value, errorMessage = null, infoMessage = null)
	}

	/**
	 * Crea una nova categoria a partir del formulari.
	 */
	fun crearCategoria() {
		val nombre = ociState.nuevaCategoriaNombre.trim()
		if (nombre.isBlank()) {
			ociState = ociState.copy(errorMessage = "El nom de la categoria no pot estar buit.", infoMessage = null)
			return
		}

		if (GestorRepositorio.repositorioCategoria.trobarPerId(nombre) != null) {
			ociState = ociState.copy(errorMessage = "La categoria '$nombre' ja existeix.", infoMessage = null)
			return
		}

		gestorOci.crearCategoria(Categoria(nombre, nombre))
		ociState = ociState.copy(
			nuevaCategoriaNombre = "",
			errorMessage = null,
			infoMessage = "Categoria creada correctament."
		)
		carregarCataleg()
	}

	/**
	 * Actualitza el formulari de nou element amb l'identificador introduït.
	 *
	 * @param value nou identificador.
	 */
	fun actualitzarNouElementId(value: String) {
		ociState = ociState.copy(nuevoElementoId = value, errorMessage = null, infoMessage = null)
	}

	/**
	 * Actualitza el formulari de nou element amb el títol introduït.
	 *
	 * @param value nou títol.
	 */
	fun actualitzarNouElementTitol(value: String) {
		ociState = ociState.copy(nuevoElementoTitulo = value, errorMessage = null, infoMessage = null)
	}

	/**
	 * Actualitza el formulari de nou element amb la descripció introduïda.
	 *
	 * @param value nova descripció.
	 */
	fun actualitzarNouElementDescripcio(value: String) {
		ociState = ociState.copy(nuevoElementoDescripcion = value, errorMessage = null, infoMessage = null)
	}

	/**
	 * Actualitza el formulari de nou element amb la categoria seleccionada.
	 *
	 * @param value identificador de la categoria.
	 */
	fun actualitzarNouElementCategoriaId(value: String) {
		ociState = ociState.copy(nuevoElementoCategoriaId = value, errorMessage = null, infoMessage = null)
	}

	/**
	 * Crea un element d'oci a partir del formulari.
	 */
	fun crearElemento() {
		val id = ociState.nuevoElementoId.trim()
		val titulo = ociState.nuevoElementoTitulo.trim()
		val descripcion = ociState.nuevoElementoDescripcion.trim()
		val categoriaId = ociState.nuevoElementoCategoriaId.trim()

		if (id.isBlank() || titulo.isBlank() || descripcion.isBlank() || categoriaId.isBlank()) {
			ociState = ociState.copy(errorMessage = "Tots els camps de l'element son obligatoris.", infoMessage = null)
			return
		}

		if (GestorRepositorio.repositorioElemento.trobarPerId(id) != null) {
			ociState = ociState.copy(errorMessage = "Ja existeix un element amb l'ID: $id", infoMessage = null)
			return
		}

		val categoria = GestorRepositorio.repositorioCategoria.trobarPerId(categoriaId)
		if (categoria == null) {
			ociState = ociState.copy(errorMessage = "No existeix la categoria '$categoriaId'.", infoMessage = null)
			return
		}

		gestorOci.crearElement(ElementOci(id, titulo, descripcion, categoria))
		ociState = ociState.copy(
			nuevoElementoId = "",
			nuevoElementoTitulo = "",
			nuevoElementoDescripcion = "",
			nuevoElementoCategoriaId = "",
			errorMessage = null,
			infoMessage = "Element creat correctament."
		)
		carregarCataleg()
	}

	/**
	 * Actualitza l'identificador de l'element que l'usuari vol gestionar.
	 *
	 * @param value identificador introduït.
	 */
	fun actualitzarElementObjectiuId(value: String) {
		ociState = ociState.copy(elementoObjetivoId = value, errorMessage = null, infoMessage = null)
	}

	/**
	 * Afegeix l'element seleccionat a la llista de l'usuari autenticat.
	 */
	fun afegirElementAUsuari() {
		val user = ociState.usuarioLogueado as? UserNormal ?: return
		try {
			val titulo = gestorOci.afegirElementAUsuari(user, ociState.elementoObjetivoId.trim())
			ociState = ociState.copy(errorMessage = null, infoMessage = "Element '$titulo' afegit correctament.", elementoObjetivoId = "")
			carregarMeusElements()
		} catch (e: ElementNoTrobatException) {
			ociState = ociState.copy(errorMessage = e.message, infoMessage = null)
		} catch (e: ElementDuplicatException) {
			ociState = ociState.copy(errorMessage = e.message, infoMessage = null)
		} catch (e: TextBuitException) {
			ociState = ociState.copy(errorMessage = e.message, infoMessage = null)
		}
	}

	/**
	 * Avança l'estat de l'element seleccionat a la llista de l'usuari.
	 */
	fun avancarEstatElement() {
		val user = ociState.usuarioLogueado as? UserNormal ?: return
		try {
			val nuevoEstado = gestorOci.avancarEstatElementUsuari(user, ociState.elementoObjetivoId.trim())
			ociState = ociState.copy(errorMessage = null, infoMessage = "Estat avancat a: $nuevoEstado")
			carregarMeusElements()
		} catch (e: Exception) {
			ociState = ociState.copy(errorMessage = e.message, infoMessage = null)
		}
	}

	/**
	 * Retrocedeix l'estat de l'element seleccionat a la llista de l'usuari.
	 */
	fun retrocedirEstatElement() {
		val user = ociState.usuarioLogueado as? UserNormal ?: return
		try {
			val nuevoEstado = gestorOci.retrocedirEstatElementUsuari(user, ociState.elementoObjetivoId.trim())
			ociState = ociState.copy(errorMessage = null, infoMessage = "Estat retrocedit a: $nuevoEstado")
			carregarMeusElements()
		} catch (e: Exception) {
			ociState = ociState.copy(errorMessage = e.message, infoMessage = null)
		}
	}

	/**
	 * Neteja els missatges informatius i d'error de la pantalla.
	 */
	fun netejarMissatges() {
		ociState = ociState.copy(errorMessage = null, infoMessage = null)
	}

	/**
	 * Tanca la sessió i reinicia tot l'estat de la UI.
	 */
	fun tancarSessio() {
		ociState = OciState()
	}
}
package ui.viewmodel

import app.GestorOci
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import exceptions.*
import models.*
import repository.*

data class UserSummary(
    val username: String,
    val display: String,
    val rol: String
)

data class UserElementItem(
    val elementOciId: String,
    val titulo: String,
    val estado: String
)

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

class ViewModel {
	private val gestorOci = GestorOci()

	var ociState by mutableStateOf(OciState())
		private set

	fun actualitzarUsuari(usuari: String) {
		ociState = ociState.copy(usuario = usuari, errorMessage = null, infoMessage = null)
	}

	fun actualitzarContrasenya(contrasenya: String) {
		ociState = ociState.copy(contrasena = contrasenya, errorMessage = null, infoMessage = null)
	}

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

	fun carregarUsuaris() {
		val usuaris = GestorRepositorio.repositorioUsuario.trobarTots().map {
			UserSummary(it.username, it.display, rolDeUsuari(it))
		}
		ociState = ociState.copy(usuarios = usuaris)
	}

	fun carregarCataleg() {
		ociState = ociState.copy(
			categorias = GestorRepositorio.repositorioCategoria.trobarTots(),
			elementos = GestorRepositorio.repositorioElemento.trobarTots()
		)
	}

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

	fun actualitzarNouNomUsuari(value: String) {
		ociState = ociState.copy(nuevoUsername = value, errorMessage = null, infoMessage = null)
	}

	fun actualitzarNovaContrasenya(value: String) {
		ociState = ociState.copy(nuevoPassword = value, errorMessage = null, infoMessage = null)
	}

	fun actualitzarNouDisplay(value: String) {
		ociState = ociState.copy(nuevoDisplay = value, errorMessage = null, infoMessage = null)
	}

	fun actualitzarNouEsAdmin(value: Boolean) {
		ociState = ociState.copy(nuevoEsAdmin = value)
	}

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

	fun actualitzarNomNovaCategoria(value: String) {
		ociState = ociState.copy(nuevaCategoriaNombre = value, errorMessage = null, infoMessage = null)
	}

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

	fun actualitzarNouElementId(value: String) {
		ociState = ociState.copy(nuevoElementoId = value, errorMessage = null, infoMessage = null)
	}

	fun actualitzarNouElementTitol(value: String) {
		ociState = ociState.copy(nuevoElementoTitulo = value, errorMessage = null, infoMessage = null)
	}

	fun actualitzarNouElementDescripcio(value: String) {
		ociState = ociState.copy(nuevoElementoDescripcion = value, errorMessage = null, infoMessage = null)
	}

	fun actualitzarNouElementCategoriaId(value: String) {
		ociState = ociState.copy(nuevoElementoCategoriaId = value, errorMessage = null, infoMessage = null)
	}

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

	fun actualitzarElementObjectiuId(value: String) {
		ociState = ociState.copy(elementoObjetivoId = value, errorMessage = null, infoMessage = null)
	}

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

	fun avançarEstatElement() {
		val user = ociState.usuarioLogueado as? UserNormal ?: return
		try {
			val nuevoEstado = gestorOci.avancarEstatElementUsuari(user, ociState.elementoObjetivoId.trim())
			ociState = ociState.copy(errorMessage = null, infoMessage = "Estat avancat a: $nuevoEstado")
			carregarMeusElements()
		} catch (e: Exception) {
			ociState = ociState.copy(errorMessage = e.message, infoMessage = null)
		}
	}

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

	fun netejarMissatges() {
		ociState = ociState.copy(errorMessage = null, infoMessage = null)
	}

	fun tancarSessio() {
		ociState = OciState()
	}
}
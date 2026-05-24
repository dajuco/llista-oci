package repository

import models.*
import kotlinx.serialization.serializer

object GestorRepositorio {
    private val apiBase = System.getenv("API_URL") ?: "http://localhost:3000"

    val repositorioElemento: Repositorio<ElementOci> = RepositorioApi(
        apiBase,
        "elementos",
        serializer<ElementOci>()
    )

    val repositorioCategoria: Repositorio<Categoria> = RepositorioApi(
        apiBase,
        "categorias",
        serializer<Categoria>()
    )

    val repositorioUsuario: Repositorio<User> = RepositorioApi(
        apiBase,
        "usuarios",
        serializer<User>()
    )
}

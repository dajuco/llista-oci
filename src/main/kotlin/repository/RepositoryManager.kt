package repository

import models.*
import kotlinx.serialization.serializer

object GestorRepositorio {
    val repositorioElemento: Repositorio<ElementOci> = RepositorioJson(
        "src/main/kotlin/jsons/elementos.json",
        serializer<ElementOci>()
    )

    val repositorioCategoria: Repositorio<Categoria> = RepositorioJson(
        "src/main/kotlin/jsons/categorias.json",
        serializer<Categoria>()
    )

    val repositorioUsuario: Repositorio<User> = RepositorioJson(
        "src/main/kotlin/jsons/usuarios.json",
        serializer<User>()
    )
}

package repository

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File

class RepositorioJson<T> (
    private val archivo: String,
    private val serializador: KSerializer<T>

) : Repositorio<T> {

    private val json = Json { prettyPrint = true }

    private fun llegirDesdeArxiu(): MutableList<T> {
        val archivo = File(archivo)
        if (!archivo.exists()) {
            return mutableListOf()
        }

        val contenido = archivo.readText()
        if (contenido.isBlank()) return mutableListOf()
        return json.decodeFromString(
            ListSerializer(serializador),
            contenido
        ).toMutableList()
    }

    private fun escribirEnArchivo(elementos: List<T>) {
        val archivo = File(archivo)
        val cadenaJson = json.encodeToString(
            ListSerializer(serializador),
            elementos
        )
        archivo.writeText(cadenaJson)
    }



    override fun desar(element: T) {
        val elementos = llegirDesdeArxiu()
        elementos.add(element)
        escribirEnArchivo(elementos)
    }

    override fun trobarTots(): List<T> {
        return llegirDesdeArxiu()
    }

    override fun eliminar(id: String) {
        val elementos = llegirDesdeArxiu()

        val filtrados = elementos.filterNot {
            val idElemento = it!!::class.members
                .firstOrNull { m -> m.name == "id" }
                ?.call(it) as? String

            idElemento == id
        }

        escribirEnArchivo(filtrados)
    }

    override fun actualitzar(element: T): Boolean {
        val elementos = llegirDesdeArxiu()

        val idElemento = element!!::class.members
            .firstOrNull { m -> m.name == "id" }
            ?.call(element) as? String ?: return false

        val indice = elementos.indexOfFirst {
            val idActual = it!!::class.members
                .firstOrNull { m -> m.name == "id" }
                ?.call(it) as? String

            idActual == idElemento
        }

        if (indice != -1) {
            elementos[indice] = element
            escribirEnArchivo(elementos)
            return true
        }

        return false
    }

    override fun trobarPerId(id: String): T? {
        val elementos = llegirDesdeArxiu()
        return elementos.find {
            val idActual = it!!::class.members
                .firstOrNull { m -> m.name == "id" }
                ?.call(it) as? String

            idActual == id
        }
    }

    override fun trobarPerTitol(titol: String): T? {
        val elementos = llegirDesdeArxiu()
        return elementos.find {
            val tituloActual = it!!::class.members
                .firstOrNull { m -> m.name == "titulo" }
                ?.call(it) as? String

            tituloActual == titol
        }

    }

    override fun trobarPerUsuari(usuari: String): T? {
        val elementos = llegirDesdeArxiu()
        return elementos.find {
            val usuarioActual = it!!::class.members
                .firstOrNull { m -> m.name == "username" }
                ?.call(it) as? String

            usuarioActual == usuari
        }

    }
}
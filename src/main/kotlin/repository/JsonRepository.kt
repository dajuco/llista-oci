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

    private fun leerDesdeArchivo(): MutableList<T> {
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



    override fun guardar(elemento: T) {
        val elementos = leerDesdeArchivo()
        elementos.add(elemento)
        escribirEnArchivo(elementos)
    }

    override fun encontrarTodos(): List<T> {
        return leerDesdeArchivo()
    }

    override fun eliminar(id: String) {
        val elementos = leerDesdeArchivo()

        val filtrados = elementos.filterNot {
            val idElemento = it!!::class.members
                .firstOrNull { m -> m.name == "id" }
                ?.call(it) as? String

            idElemento == id
        }

        escribirEnArchivo(filtrados)
    }

    override fun actualizar(elemento: T): Boolean {
        val elementos = leerDesdeArchivo()

        val idElemento = elemento!!::class.members
            .firstOrNull { m -> m.name == "id" }
            ?.call(elemento) as? String ?: return false

        val indice = elementos.indexOfFirst {
            val idActual = it!!::class.members
                .firstOrNull { m -> m.name == "id" }
                ?.call(it) as? String

            idActual == idElemento
        }

        if (indice != -1) {
            elementos[indice] = elemento
            escribirEnArchivo(elementos)
            return true
        }

        return false
    }

    override fun encontrarPorId(id: String): T? {
        val elementos = leerDesdeArchivo()
        return elementos.find {
            val idActual = it!!::class.members
                .firstOrNull { m -> m.name == "id" }
                ?.call(it) as? String

            idActual == id
        }
    }

    override fun encontrarPorTitulo(titulo: String): T? {
        val elementos = leerDesdeArchivo()
        return elementos.find {
            val tituloActual = it!!::class.members
                .firstOrNull { m -> m.name == "titulo" }
                ?.call(it) as? String

            tituloActual == titulo
        }

    }

    override fun encontrarPorUser(usuario: String): T? {
        val elementos = leerDesdeArchivo()
        return elementos.find {
            val usuarioActual = it!!::class.members
                .firstOrNull { m -> m.name == "username" }
                ?.call(it) as? String

            usuarioActual == usuario
        }

    }
}
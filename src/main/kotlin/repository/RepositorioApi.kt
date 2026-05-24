package repository

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.reflect.full.memberProperties

/**
 * Repositori que fa peticions a una API REST per persistència en lloc de fitxers JSON.
 *
 * Aquest repositori és genèric i mapeja operacions CRUD als endpoints REST:
 * - GET  {baseUrl}/{path}           -> obtenir tots
 * - GET  {baseUrl}/{path}/{id}      -> obtenir per id
 * - POST {baseUrl}/{path}           -> crear
 * - PUT  {baseUrl}/{path}/{id}      -> actualitzar
 * - DELETE {baseUrl}/{path}/{id}    -> eliminar
 *
 * Per fer servir-lo, passa la URL base (p. ex. "http://localhost:3000"), el path
 * (p. ex. "elementos") i el serialitzador de Kotlinx.
 *
 * Les operacions de cerca per títol/usuari es fan client-side (es descarreguen tots
 * i s'aplica el filtre) perquè l'API REST no exposa consultes específiques.
 */
class RepositorioApi<T>(
    private val baseUrl: String,
    private val path: String,
    private val serializador: KSerializer<T>,
    private val client: HttpClient = HttpClient()
) : Repositorio<T> {

    private val json = Json { ignoreUnknownKeys = true; prettyPrint = true }

    private fun url() = "${baseUrl.trimEnd('/')}/$path"

    override fun desar(element: T) {
        runBlocking {
            val body = json.encodeToString(serializador, element)
            client.post(url()) {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }
    }

    override fun trobarTots(): List<T> {
        return runBlocking {
            val resp: HttpResponse = client.get(url())
            val text = resp.bodyAsText()
            if (text.isBlank()) return@runBlocking emptyList<T>()
            json.decodeFromString(ListSerializer(serializador), text)
        }
    }

    override fun eliminar(id: String) {
        runBlocking {
            client.delete("${url().trimEnd('/')}/$id")
        }
    }

    override fun actualitzar(element: T): Boolean {
        val idElemento = element!!::class.memberProperties
            .firstOrNull { it.name == "id" }
            ?.getter
            ?.call(element) as? String ?: return false

        return runBlocking {
            val body = json.encodeToString(serializador, element)
            val resp = client.put("${url().trimEnd('/')}/$idElemento") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            resp.status.isSuccess()
        }
    }

    override fun trobarPerId(id: String): T? {
        return runBlocking {
            val resp: HttpResponse = client.get("${url().trimEnd('/')}/$id")
            if (resp.status == HttpStatusCode.NotFound) return@runBlocking null
            val text = resp.bodyAsText()
            if (text.isBlank()) return@runBlocking null
            json.decodeFromString(serializador, text)
        }
    }

    override fun trobarPerTitol(titol: String): T? {
        val tots = trobarTots()
        return tots.find {
            val prop = it!!::class.memberProperties.firstOrNull { p -> p.name == "titulo" }
            val valor = prop?.getter?.call(it) as? String
            valor == titol
        }
    }

    override fun trobarPerUsuari(usuari: String): T? {
        val tots = trobarTots()
        return tots.find {
            val prop = it!!::class.memberProperties.firstOrNull { p -> p.name == "username" }
            val valor = prop?.getter?.call(it) as? String
            valor == usuari
        }
    }
}



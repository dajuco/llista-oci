# 📚 2. Implementación Paso a Paso

## PASO 1: Dependencia de Serialización

Ya deberías tenerla en `build.gradle.kts`:

```kotlin
dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
}
```

✅ Si compilas sin errores, está todo bien.

---

## PASO 2: Crear la Interfaz Repository

Crea el archivo: **`src/main/kotlin/repository/Repository.kt`**

```kotlin
package repository

interface Repository<T> {
    fun save(item: T): Boolean
    fun findAll(): List<T>
    fun findById(id: String): T?
    fun delete(id: String): Boolean
    fun update(item: T): Boolean
}
```

**Explicación:**
- `save()`: Crea un nuevo elemento
- `findAll()`: Obtiene todos los elementos
- `findById()`: Busca uno por id
- `delete()`: Elimina por id
- `update()`: Modifica uno existente

---

## PASO 3: Crear JsonRepository<T>

Crea el archivo: **`src/main/kotlin/repository/JsonRepository.kt`**

```kotlin
package repository

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File

class JsonRepository<T>(
    private val fileName: String,
    private val serializer: KSerializer<T>
) : Repository<T> {

    private val json = Json { prettyPrint = true }

    private fun readFromFile(): MutableList<T> {
        val file = File(fileName)
        if (!file.exists()) return mutableListOf()

        val content = file.readText()
        if (content.isBlank()) return mutableListOf()

        return json.decodeFromString(
            ListSerializer(serializer),
            content
        ).toMutableList()
    }

    private fun writeToFile(items: List<T>) {
        val file = File(fileName)
        val jsonString = json.encodeToString(
            ListSerializer(serializer),
            items
        )
        file.writeText(jsonString)
    }

    override fun save(item: T): Boolean {
        return try {
            val items = readFromFile()
            items.add(item)
            writeToFile(items)
            true
        } catch (e: Exception) {
            println("Error al guardar: ${e.message}")
            false
        }
    }

    override fun findAll(): List<T> {
        return readFromFile()
    }

    override fun findById(id: String): T? {
        return readFromFile().firstOrNull {
            val itemId = it!!::class.members
                .firstOrNull { m -> m.name == "id" }
                ?.call(it) as? String
            itemId == id
        }
    }

    override fun delete(id: String): Boolean {
        return try {
            val items = readFromFile()
            val filtered = items.filterNot {
                val itemId = it!!::class.members
                    .firstOrNull { m -> m.name == "id" }
                    ?.call(it) as? String
                itemId == id
            }
            writeToFile(filtered)
            true
        } catch (e: Exception) {
            println("Error al eliminar: ${e.message}")
            false
        }
    }

    override fun update(item: T): Boolean {
        return try {
            val items = readFromFile()
            val index = items.indexOfFirst {
                val itemId = it!!::class.members
                    .firstOrNull { m -> m.name == "id" }
                    ?.call(it) as? String
                val newId = item!!::class.members
                    .firstOrNull { m -> m.name == "id" }
                    ?.call(item) as? String
                itemId == newId
            }
            if (index != -1) {
                items[index] = item
                writeToFile(items)
                true
            } else false
        } catch (e: Exception) {
            println("Error al actualizar: ${e.message}")
            false
        }
    }
}
```

**Explicación del código:**
- `readFromFile()`: Lee el JSON y lo convierte a Lista Kotlin
- `writeToFile()`: Convierte la Lista a JSON y la escribe
- `save()`: Lee, añade, escribe
- `findAll()`: Solo lee
- `findById()`: Lee y busca por id
- `delete()`: Lee, filtra, escribe (sin el eliminado)
- `update()`: Lee, busca el índice, reemplaza, escribe

---

## PASO 4: Añadir @Serializable a tus Modelos

Modifica **`src/main/kotlin/models/ElementOci.kt`**:

```kotlin
package models

import kotlinx.serialization.Serializable

@Serializable
data class ElementOci(
    val id: String,
    val titulo: String,
    var descripcion: String,
    val categoria: Categoria
)
```

Modifica **`src/main/kotlin/models/Categoria.kt`**:

```kotlin
package models

import kotlinx.serialization.Serializable

@Serializable
data class Categoria(val nombre: String)
```

Modifica **`src/main/kotlin/models/User.kt`**:

```kotlin
package models

import kotlinx.serialization.Serializable

@Serializable
open class User(
    val id: String,
    val username: String,
    val password: String,
    val display: String
)

@Serializable
data class UserAdmin(
    val id: String,
    val username: String,
    val password: String,
    val display: String
) : User(id, username, password, display)

@Serializable
data class UserNormal(
    val id: String,
    val username: String,
    val password: String,
    val display: String
) : User(id, username, password, display)

@Serializable
data class UserSuperAdmin(
    val id: String,
    val username: String,
    val password: String,
    val display: String
) : User(id, username, password, display)
```

**⚠️ IMPORTANTE:**
- Todo modelo que quieras serializar a JSON necesita `@Serializable`
- Si un modelo tiene propiedades de otros modelos, esos también necesitan `@Serializable`
- Las enums también necesitan `@Serializable`

---

## PASO 5: Crear la Carpeta data/

Crea la carpeta en la **raíz del proyecto**:

```bash
mkdir -p data
```

Estructura final:
```
llista-oci/
├── data/                  ← Carpeta para JSONs
│   ├── elementos.json
│   ├── categorias.json
│   └── usuarios.json
├── src/
├── docs/
└── ...
```

---

## PASO 6: Crear los Archivos JSON Vacíos

Crea **`data/elementos.json`**:
```json
[]
```

Crea **`data/categorias.json`**:
```json
[]
```

Crea **`data/usuarios.json`**:
```json
[]
```

---

## PASO 7: Instanciar los Repositories en GestorOci

Modifica **`src/main/kotlin/app/GestorOci.kt`**:

```kotlin
package app

import repository.Repository
import repository.JsonRepository
import models.*
import kotlinx.serialization.serializer

class GestorOci {

    // Crea los repositorios para cada entidad
    private val elementoRepository: Repository<ElementOci> = JsonRepository(
        "data/elementos.json",
        serializer<ElementOci>()
    )

    private val categoriaRepository: Repository<Categoria> = JsonRepository(
        "data/categorias.json",
        serializer<Categoria>()
    )

    private val usuarioRepository: Repository<User> = JsonRepository(
        "data/usuarios.json",
        serializer<User>()
    )

    // Ahora puedes usar los repositorios
    fun crearElemento(id: String, titulo: String, descripcion: String, categoria: Categoria) {
        val elemento = ElementOci(id, titulo, descripcion, categoria)

        if (elementoRepository.save(elemento)) {
            println("✅ Elemento creado!")
        } else {
            println("❌ Error al crear elemento")
        }
    }

    fun listarElementos() {
        val elementos = elementoRepository.findAll()
        if (elementos.isEmpty()) {
            println("No hay elementos")
        } else {
            elementos.forEach { println(it) }
        }
    }

    fun crearCategoria(nombre: String) {
        val categoria = Categoria(nombre)

        if (categoriaRepository.save(categoria)) {
            println("✅ Categoría creada!")
        } else {
            println("❌ Error al crear categoría")
        }
    }

    fun listarCategorias() {
        val categorias = categoriaRepository.findAll()
        if (categorias.isEmpty()) {
            println("No hay categorías")
        } else {
            categorias.forEach { println(it) }
        }
    }
}
```

---

## PASO 8: Prueba

En tu `Main.kt`:

```kotlin
import app.GestorOci
import models.Categoria

fun main() {
    val gestor = GestorOci()

    // Crear una categoría
    gestor.crearCategoria("Libros")
    gestor.crearCategoria("Películas")

    // Listar categorías
    println("=== CATEGORÍAS ===")
    gestor.listarCategorias()

    println()
}
```

Si ves esto:
```
✅ Categoría creada!
✅ Categoría creada!
=== CATEGORÍAS ===
Categoria(nombre=Libros)
Categoria(nombre=Películas)
```

¡**Todo está funcionando!** ✅

---

## ✓ CHECKLIST

- [x] Dependencia añadida en `build.gradle.kts`
- [x] Interfaz `Repository.kt` creada
- [x] Clase `JsonRepository.kt` creada
- [x] `@Serializable` añadido a los modelos
- [x] Carpeta `data/` creada
- [x] Archivos JSON creados (vacíos)
- [x] Repositories instanciados en `GestorOci`
- [x] Probado en `Main.kt`

---

## SIGUIENTE

👉 Lee: [`03_MULTIPLES_REPOSITORIES.md`](./03_MULTIPLES_REPOSITORIES.md)

Esta guía profundiza en cómo tener múltiples JSONs organizados, que es exactamente lo que necesitas.


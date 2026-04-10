# 📚 6. Troubleshooting - Problemas Comunes

## 🔴 Problema 1: "Cannot inline bytecode built with JVM target 1.8 into bytecode that is being built with JVM target 21"

### Causa
El Gradle está usando una versión de Kotlin incompatible con Java 21.

### Solución
En `build.gradle.kts`:
```kotlin
kotlin {
    jvmToolchain(21)  // ← Asegúrate de que esto está configurado
}
```

---

## 🔴 Problema 2: "No serializer found for type T"

### Causa
Falta `@Serializable` en tu modelo.

### Solución
```kotlin
import kotlinx.serialization.Serializable

@Serializable  // ← Añade esto
data class ElementOci(
    val id: String,
    val titulo: String,
    val categoria: Categoria
)

@Serializable  // ← La categoría también necesita esto
data class Categoria(val nombre: String)
```

---

## 🔴 Problema 3: "Cannot find symbol: serializer"

### Causa
Falta el import de `serializer`.

### Solución
En tu archivo donde uses repositories:
```kotlin
import kotlinx.serialization.serializer  // ← Añade este import

// Luego:
val repo = JsonRepository("archivo.json", serializer<ElementOci>())
```

---

## 🔴 Problema 4: "File not found: data/elementos.json"

### Causa
La carpeta `data/` no existe o el archivo no está en el lugar correcto.

### Solución
```bash
# Desde la raíz del proyecto (llista-oci/):
mkdir -p data

# Crea los archivos JSON vacíos:
echo "[]" > data/elementos.json
echo "[]" > data/categorias.json
echo "[]" > data/usuarios.json
```

Estructura correcta:
```
llista-oci/
├── data/
│   ├── elementos.json
│   ├── categorias.json
│   └── usuarios.json
├── src/
└── ...
```

---

## 🔴 Problema 5: "JSON is not a valid format"

### Causa
Hay un error de sintaxis en el archivo JSON.

### Solución
Los JSONs siempre deben ser **arrays válidos**:

```json
// ✅ CORRECTO (array vacío)
[]

// ✅ CORRECTO (array con objetos)
[
  { "id": "1", "nombre": "Juan" },
  { "id": "2", "nombre": "María" }
]

// ❌ INCORRECTO (no es un array)
{ "id": "1", "nombre": "Juan" }

// ❌ INCORRECTO (JSON malformado)
[ { "id": 1, "nombre": Juan } ]
```

---

## 🔴 Problema 6: "Serialization failed for 'Categoria'" o errores similares

### Causa
Hay una clase dentro de tu modelo que no tiene `@Serializable`.

### Solución
Asegúrate de que TODAS las clases que se serialicen tienen `@Serializable`:

```kotlin
@Serializable  // ← El elemento
data class ElementOci(
    val id: String,
    val titulo: String,
    val categoria: Categoria  // ← Categoria también necesita @Serializable
)

@Serializable  // ← Así
data class Categoria(val nombre: String)
```

---

## 🔴 Problema 7: "Update returns false (elemento no se actualiza)"

### Causa
Probablemente el `id` no coincide o hay un error de reflexión.

### Solución
Verifica que:
1. El objeto tiene un campo `id` con ese nombre exacto:
```kotlin
data class ElementOci(
    val id: String,  // ← Debe existir y llamarse exactamente "id"
    val titulo: String
)
```

2. El objeto encontrado con `findById()` es el correcto:
```kotlin
val elemento = elementoRepository.findById("123")
if (elemento != null) {
    val modificado = elemento.copy(titulo = "Nuevo")
    val resultado = elementoRepository.update(modificado)
    println("Update: $resultado")  // Si es false, algo está mal
}
```

---

## 🔴 Problema 8: "Cambios en memoria pero no se guardan en JSON"

### Causa
Olvidaste llamar a `save()` o `update()`.

### Solución
```kotlin
// ❌ INCORRECTO
val usuario = usuarioRepository.findById("1")
usuario.elementos.add(nuevoElemento)
// ← El elemento está en memoria, pero NO en el JSON

// ✅ CORRECTO
val usuario = usuarioRepository.findById("1")
usuario.elementos.add(nuevoElemento)
usuarioRepository.update(usuario)  // ← GUARDAR
```

---

## 🔴 Problema 9: "MutableList desaparece después de recargar"

### Causa
Las listas están en memoria, no en el JSON.

### Solución
Siempre actualiza el usuario después de modificar su lista:

```kotlin
// Paso 1: Obtener
val usuario = usuarioRepository.findById(usuarioId)

// Paso 2: Modificar la lista
usuario.elementos.add(nuevoElemento)

// Paso 3: GUARDAR en JSON
usuarioRepository.update(usuario)

// Paso 4: Recargar desde JSON (opcional)
val usuarioActualizado = usuarioRepository.findById(usuarioId)
```

---

## 🔴 Problema 10: "Pretty print hace el JSON ilegible"

### Solución
En `JsonRepository.kt`, el `prettyPrint` ya está habilitado:

```kotlin
private val json = Json { prettyPrint = true }  // ← Esto hace el JSON legible
```

Si quieres deshabilitarlo (JSON comprimido):
```kotlin
private val json = Json { prettyPrint = false }
```

---

## 🔴 Problema 11: "La reflexión es lenta"

### Causa
Usar reflexión para acceder al `id` cada vez es ineficiente.

### Solución (Para después)
Mejorar con una interfaz específica:

```kotlin
interface Identifiable {
    val id: String
}

data class ElementOci(
    override val id: String,
    val titulo: String
) : Identifiable
```

Así la búsqueda es más rápida. Pero por ahora, con reflexión funciona.

---

## 🔴 Problema 12: "¿Cómo debuggear?"

### Solución
Añade mensajes de debug:

```kotlin
override fun update(item: T): Boolean {
    return try {
        val items = readFromFile()
        println("DEBUG: Elementos actuales: ${items.size}")

        val index = items.indexOfFirst { /* búsqueda */ }
        println("DEBUG: Índice encontrado: $index")

        if (index != -1) {
            items[index] = item
            writeToFile(items)
            println("DEBUG: Guardado correctamente")
            true
        } else {
            println("DEBUG: No encontrado")
            false
        }
    } catch (e: Exception) {
        println("DEBUG: Error - ${e.message}")
        e.printStackTrace()
        false
    }
}
```

---

## 🔴 Problema 13: "Herencia no serializa correctamente"

### Causa
Las clases heredadas no están bien configuradas con `@Serializable`.

### Solución
```kotlin
@Serializable
open class User(
    val id: String,
    val username: String
)

@Serializable
data class UserAdmin(
    val id: String,
    val username: String,
    val adminLevel: Int = 0
) : User(id, username)  // ← Asegúrate de pasar los parámetros correctos
```

---

## 🔴 Problema 14: "El JSON contiene null o valores vacíos"

### Causa
Probablemente el modelo tiene propiedades que son null.

### Solución
Asegúrate de que todos los parámetros tienen valor:

```kotlin
// ❌ INCORRECTO
val elemento = ElementOci("", "", "")  // ← IDs vacíos

// ✅ CORRECTO
val elemento = ElementOci(
    id = "elem_${System.currentTimeMillis()}",
    titulo = "Titulo",
    descripcion = "Descripción"
)
```

---

## 🟡 CONSEJO: Usar try-catch

Envuelve tu código con try-catch para ver errores:

```kotlin
try {
    elementoRepository.save(elemento)
} catch (e: Exception) {
    println("Error: ${e.message}")
    e.printStackTrace()
}
```

---

## 📞 Checklist de Debug

Si algo no funciona:

- [ ] ¿Todos los modelos tienen `@Serializable`?
- [ ] ¿Los modelos anidados también tienen `@Serializable`?
- [ ] ¿Existe la carpeta `data/` en la raíz?
- [ ] ¿Los archivos JSON existen y tienen `[]`?
- [ ] ¿Has llamado a `save()` o `update()` después de cambios?
- [ ] ¿El `id` existe y se llama exactamente `id`?
- [ ] ¿Hay errores de compilación? (revisa `build.gradle.kts`)
- [ ] ¿La dependencia de serialización está instalada?

Si aún así no funciona, copia el error completo y pregunta.

---

## SIGUIENTE

👉 Vuelve al [`00_INDICE.md`](./00_INDICE.md) para leer otras guías o si tienes otra pregunta.


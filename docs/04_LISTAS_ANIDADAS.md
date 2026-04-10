# 📚 4. Listas Anidadas: Usuarios con sus Elementos

## ¿CUÁL ES TU NECESIDAD?

Quieres que **cada usuario tenga su propia lista de elementos**.

Por ejemplo:
```
usuario1 (Juan)
  ├─ elemento A (su libro favorito)
  ├─ elemento B (su película favorita)
  └─ elemento C (su videojuego favorito)

usuario2 (María)
  ├─ elemento X
  └─ elemento Y
```

---

## COMPARATIVA: ANTES vs DESPUÉS

### SIN Listas Anidadas (Lo que ya hiciste)
```json
data/usuarios.json
[
  { "id": "1", "username": "juan", ... },
  { "id": "2", "username": "maria", ... }
]

data/elementos.json
[
  { "id": "a", "titulo": "Libro1", "owner": "1", ... },
  { "id": "b", "titulo": "Libro2", "owner": "2", ... }
]
```
**Problema:** Los elementos están separados del usuario, necesitas una relación adicional.

### CON Listas Anidadas (Lo que vas a hacer)
```json
data/usuarios.json
[
  {
    "id": "1",
    "username": "juan",
    "elementos": [
      { "id": "a", "titulo": "Libro1", ... },
      { "id": "b", "titulo": "Libro2", ... }
    ]
  },
  {
    "id": "2",
    "username": "maria",
    "elementos": [
      { "id": "x", "titulo": "Film1", ... }
    ]
  }
]
```
**Ventaja:** Todo el usuario y sus elementos en un solo lugar.

---

## PASO 1: Modificar los Modelos

### Modificar ElementOci (sin cambios grandes, solo añadir lo necesario)

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

### Modificar User para que contenga una lista

```kotlin
package models

import kotlinx.serialization.Serializable

@Serializable
open class User(
    val id: String,
    val username: String,
    val password: String,
    val display: String,
    val elementos: MutableList<ElementOci> = mutableListOf()  // ← Lista de elementos
)

@Serializable
data class UserAdmin(
    val id: String,
    val username: String,
    val password: String,
    val display: String,
    val elementos: MutableList<ElementOci> = mutableListOf()
) : User(id, username, password, display, elementos)

@Serializable
data class UserNormal(
    val id: String,
    val username: String,
    val password: String,
    val display: String,
    val elementos: MutableList<ElementOci> = mutableListOf()
) : User(id, username, password, display, elementos)

@Serializable
data class UserSuperAdmin(
    val id: String,
    val username: String,
    val password: String,
    val display: String,
    val elementos: MutableList<ElementOci> = mutableListOf()
) : User(id, username, password, display, elementos)
```

**Explicación:**
- Añadimos `val elementos: MutableList<ElementOci> = mutableListOf()`
- Esto significa que cada usuario TIENE una lista de elementos
- La lista comienza vacía por defecto

---

## PASO 2: Operaciones CRUD con Listas Anidadas

Ahora en `GestorOci.kt` necesitas métodos para:

### A) Crear un usuario (sin elementos)

```kotlin
fun crearUsuario(username: String, password: String, display: String, tipo: String) {
    val usuario = when (tipo.lowercase()) {
        "admin" -> UserAdmin(
            id = "user_${System.currentTimeMillis()}",
            username = username,
            password = password,
            display = display,
            elementos = mutableListOf()
        )
        "normal" -> UserNormal(
            id = "user_${System.currentTimeMillis()}",
            username = username,
            password = password,
            display = display,
            elementos = mutableListOf()
        )
        else -> {
            println("❌ Tipo de usuario desconocido")
            return
        }
    }

    if (usuarioRepository.save(usuario)) {
        println("✅ Usuario '${usuario.username}' creado!")
    } else {
        println("❌ Error al crear usuario")
    }
}
```

### B) Añadir un elemento a un usuario

```kotlin
fun agregarElementoAUsuario(usuarioId: String, titulo: String, descripcion: String, categoriaNombre: String) {
    // 1. Buscamos el usuario
    val usuario = usuarioRepository.findById(usuarioId)
    if (usuario == null) {
        println("❌ Usuario no encontrado")
        return
    }

    // 2. Buscamos la categoría
    val categorias = categoriaRepository.findAll()
    val categoria = categorias.firstOrNull { it.nombre == categoriaNombre }
    if (categoria == null) {
        println("❌ Categoría no existe")
        return
    }

    // 3. Creamos el nuevo elemento
    val nuevoElemento = ElementOci(
        id = "elem_${System.currentTimeMillis()}",
        titulo = titulo,
        descripcion = descripcion,
        categoria = categoria
    )

    // 4. Lo añadimos a la lista del usuario
    usuario.elementos.add(nuevoElemento)

    // 5. Actualizamos el usuario (SAVE o UPDATE)
    // Si ya existe el usuario, es UPDATE
    if (usuarioRepository.update(usuario)) {
        println("✅ Elemento '${nuevoElemento.titulo}' añadido a ${usuario.username}!")
    } else {
        println("❌ Error al actualizar usuario")
    }
}
```

### C) Listar elementos de un usuario

```kotlin
fun listarElementosDelUsuario(usuarioId: String) {
    val usuario = usuarioRepository.findById(usuarioId)
    if (usuario == null) {
        println("❌ Usuario no encontrado")
        return
    }

    if (usuario.elementos.isEmpty()) {
        println("${usuario.username} no tiene elementos")
    } else {
        println("Elementos de ${usuario.username}:")
        usuario.elementos.forEach {
            println("  - ${it.titulo}: ${it.descripcion} (${it.categoria.nombre})")
        }
    }
}
```

### D) Eliminar un elemento de un usuario

```kotlin
fun eliminarElementoDelUsuario(usuarioId: String, elementoId: String) {
    // 1. Buscamos el usuario
    val usuario = usuarioRepository.findById(usuarioId)
    if (usuario == null) {
        println("❌ Usuario no encontrado")
        return
    }

    // 2. Buscamos el elemento en su lista
    val elemento = usuario.elementos.firstOrNull { it.id == elementoId }
    if (elemento == null) {
        println("❌ Elemento no encontrado en la lista del usuario")
        return
    }

    // 3. Lo eliminamos de la lista
    usuario.elementos.remove(elemento)

    // 4. Actualizamos el usuario
    if (usuarioRepository.update(usuario)) {
        println("✅ Elemento '${elemento.titulo}' eliminado!")
    } else {
        println("❌ Error al actualizar usuario")
    }
}
```

### E) Actualizar un elemento de un usuario

```kotlin
fun actualizarElementoDelUsuario(usuarioId: String, elementoId: String, nuevoTitulo: String, nuevaDescripcion: String) {
    // 1. Buscamos el usuario
    val usuario = usuarioRepository.findById(usuarioId)
    if (usuario == null) {
        println("❌ Usuario no encontrado")
        return
    }

    // 2. Buscamos el elemento
    val index = usuario.elementos.indexOfFirst { it.id == elementoId }
    if (index == -1) {
        println("❌ Elemento no encontrado")
        return
    }

    // 3. Actualizamos el elemento
    val elementoAntiguo = usuario.elementos[index]
    usuario.elementos[index] = elementoAntiguo.copy(
        titulo = nuevoTitulo,
        descripcion = nuevaDescripcion
    )

    // 4. Actualizamos el usuario
    if (usuarioRepository.update(usuario)) {
        println("✅ Elemento actualizado!")
    } else {
        println("❌ Error al actualizar")
    }
}
```

---

## PASO 3: Flujo Completo de Prueba

```kotlin
fun main() {
    val gestor = GestorOci()

    // 1. Crear categorías
    println("=== CREANDO CATEGORÍAS ===")
    gestor.crearCategoria("Libros")
    gestor.crearCategoria("Películas")

    // 2. Crear usuarios
    println("\n=== CREANDO USUARIOS ===")
    gestor.crearUsuario("juan", "pass123", "Juan Pérez", "normal")
    gestor.crearUsuario("maria", "pass456", "María García", "normal")

    // 3. Obtener IDs de usuarios (para poder usarlos)
    val usuarios = gestor.usuarioRepository.findAll()
    val juanId = usuarios.find { it.username == "juan" }?.id
    val mariaId = usuarios.find { it.username == "maria" }?.id

    // 4. Añadir elementos a Juan
    println("\n=== AÑADIENDO ELEMENTOS A JUAN ===")
    if (juanId != null) {
        gestor.agregarElementoAUsuario(juanId, "1984", "Novela distópica", "Libros")
        gestor.agregarElementoAUsuario(juanId, "Dune", "Novela de ciencia ficción", "Libros")
        gestor.agregarElementoAUsuario(juanId, "Inception", "Película de ciencia ficción", "Películas")
    }

    // 5. Añadir elementos a María
    println("\n=== AÑADIENDO ELEMENTOS A MARÍA ===")
    if (mariaId != null) {
        gestor.agregarElementoAUsuario(mariaId, "El Quijote", "Novela clásica", "Libros")
        gestor.agregarElementoAUsuario(mariaId, "Avatar", "Película de ciencia ficción", "Películas")
    }

    // 6. Listar elementos de cada usuario
    println("\n=== ELEMENTOS DE JUAN ===")
    if (juanId != null) gestor.listarElementosDelUsuario(juanId)

    println("\n=== ELEMENTOS DE MARÍA ===")
    if (mariaId != null) gestor.listarElementosDelUsuario(mariaId)
}
```

**Resultado esperado:**
```
=== CREANDO CATEGORÍAS ===
✅ Categoría creada!
✅ Categoría creada!

=== CREANDO USUARIOS ===
✅ Usuario 'juan' creado!
✅ Usuario 'maria' creado!

=== AÑADIENDO ELEMENTOS A JUAN ===
✅ Elemento '1984' añadido a juan!
✅ Elemento 'Dune' añadido a juan!
✅ Elemento 'Inception' añadido a juan!

=== AÑADIENDO ELEMENTOS A MARÍA ===
✅ Elemento 'El Quijote' añadido a maria!
✅ Elemento 'Avatar' añadido a maria!

=== ELEMENTOS DE JUAN ===
Elementos de juan:
  - 1984: Novela distópica (Libros)
  - Dune: Novela de ciencia ficción (Libros)
  - Inception: Película de ciencia ficción (Películas)

=== ELEMENTOS DE MARÍA ===
Elementos de maria:
  - El Quijote: Novela clásica (Libros)
  - Avatar: Película de ciencia ficción (Películas)
```

---

## PASO 4: JSON Resultante

**data/usuarios.json:**
```json
[
  {
    "id": "user_1712762345",
    "username": "juan",
    "password": "pass123",
    "display": "Juan Pérez",
    "elementos": [
      {
        "id": "elem_1712762400",
        "titulo": "1984",
        "descripcion": "Novela distópica",
        "categoria": {
          "nombre": "Libros"
        }
      },
      {
        "id": "elem_1712762401",
        "titulo": "Dune",
        "descripcion": "Novela de ciencia ficción",
        "categoria": {
          "nombre": "Libros"
        }
      },
      {
        "id": "elem_1712762402",
        "titulo": "Inception",
        "descripcion": "Película de ciencia ficción",
        "categoria": {
          "nombre": "Películas"
        }
      }
    ]
  },
  {
    "id": "user_1712762346",
    "username": "maria",
    "password": "pass456",
    "display": "María García",
    "elementos": [
      {
        "id": "elem_1712762403",
        "titulo": "El Quijote",
        "descripcion": "Novela clásica",
        "categoria": {
          "nombre": "Libros"
        }
      },
      {
        "id": "elem_1712762404",
        "titulo": "Avatar",
        "descripcion": "Película de ciencia ficción",
        "categoria": {
          "nombre": "Películas"
        }
      }
    ]
  }
]
```

¡Perfecto! Cada usuario tiene su propia lista anidada.

---

## IMPORTANTE: SERIALIZACIÓN

Asegúrate de que:
- `User` tiene `@Serializable`
- `UserAdmin`, `UserNormal`, `UserSuperAdmin` tienen `@Serializable`
- `ElementOci` tiene `@Serializable`
- `Categoria` tiene `@Serializable`

Todas las clases anidadas **DEBEN** tener `@Serializable`.

---

## VENTAJAS DE LISTAS ANIDADAS

✅ **Relación clara** - Usuario y sus elementos juntos
✅ **Integridad** - Si eliminas un usuario, sus elementos desaparecen también
✅ **Independencia** - Cada usuario es independiente
✅ **Facilidad** - Todo en un solo JSON

---

## DESVENTAJAS

⚠️ Si tienes elementos **compartidos** entre usuarios, es difícil actualizarlos en todos los usuarios a la vez.

**Solución:** Usa referencias de IDs en lugar de nesting completo (pero eso es más avanzado).

---

## SIGUIENTE

👉 Lee: [`05_SAVE_vs_UPDATE.md`](./05_SAVE_vs_UPDATE.md)

Esta guía explica cuándo usar SAVE vs UPDATE con listas anidadas.


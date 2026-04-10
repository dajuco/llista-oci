# 📚 3. Múltiples Repositories (Un JSON por Entidad)

## ¿CUÁL ES TU CASO?

Quieres:
- ✅ Un JSON para **elementos** (`elementos.json`)
- ✅ Un JSON para **categorías** (`categorias.json`)
- ✅ Un JSON para **usuarios** (`usuarios.json`)
- ✅ Cada JSON contiene un array de esa entidad

---

## ARQUITECTURA

```
APLICACIÓN (GestorOci)
    │
    ├─→ elementoRepository → data/elementos.json
    ├─→ categoriaRepository → data/categorias.json
    └─→ usuarioRepository → data/usuarios.json
```

---

## ESTRUCTURA DE CARPETAS

```
llista-oci/
├── data/                           ← Carpeta de datos
│   ├── elementos.json              ← Array de ElementOci
│   ├── categorias.json             ← Array de Categoria
│   └── usuarios.json               ← Array de User
│
├── src/main/kotlin/
│   ├── repository/
│   │   ├── Repository.kt           ← Interfaz genérica
│   │   └── JsonRepository.kt       ← Implementación genérica
│   │
│   ├── models/
│   │   ├── ElementOci.kt
│   │   ├── Categoria.kt
│   │   ├── User.kt
│   │   ├── UserAdmin.kt
│   │   ├── UserNormal.kt
│   │   └── UserSuperAdmin.kt
│   │
│   └── app/
│       └── GestorOci.kt            ← Usa todos los repos
│
└── docs/
    └── (guías como esta)
```

---

## CÓMO INSTANCIAR MÚLTIPLES REPOSITORIES

En tu `GestorOci.kt`:

```kotlin
package app

import repository.Repository
import repository.JsonRepository
import models.*
import kotlinx.serialization.serializer

class GestorOci {

    // REPOSITORIO 1: Elementos
    private val elementoRepository: Repository<ElementOci> = JsonRepository(
        "data/elementos.json",
        serializer<ElementOci>()
    )

    // REPOSITORIO 2: Categorías
    private val categoriaRepository: Repository<Categoria> = JsonRepository(
        "data/categorias.json",
        serializer<Categoria>()
    )

    // REPOSITORIO 3: Usuarios
    private val usuarioRepository: Repository<User> = JsonRepository(
        "data/usuarios.json",
        serializer<User>()
    )

    // ... resto del código
}
```

**¿Qué está pasando?**

1. Creamos **3 instancias** de `JsonRepository`
2. Cada una apunta a un **archivo JSON diferente**
3. Cada una maneja un **tipo de dato diferente** (gracias a `<T>`)
4. El mismo código `JsonRepository` funciona para los 3 tipos

---

## ARCHIVOS JSON INICIALES

### data/elementos.json
```json
[]
```

### data/categorias.json
```json
[]
```

### data/usuarios.json
```json
[]
```

---

## EJEMPLO: CREAR ELEMENTOS

```kotlin
fun crearElemento(titulo: String, descripcion: String, categoriaNombre: String) {
    // 1. Buscamos la categoría
    val categorias = categoriaRepository.findAll()
    val categoria = categorias.firstOrNull { it.nombre == categoriaNombre }

    if (categoria == null) {
        println("❌ Categoría no existe")
        return
    }

    // 2. Creamos el elemento
    val elemento = ElementOci(
        id = "elemento_${System.currentTimeMillis()}",
        titulo = titulo,
        descripcion = descripcion,
        categoria = categoria
    )

    // 3. Lo guardamos con el repositorio de elementos
    if (elementoRepository.save(elemento)) {
        println("✅ Elemento creado!")
    } else {
        println("❌ Error al crear elemento")
    }
}

fun listarElementos() {
    val elementos = elementoRepository.findAll()
    elementos.forEach { println(it) }
}
```

---

## EJEMPLO: CREAR CATEGORÍAS

```kotlin
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
    categorias.forEach { println(it) }
}
```

---

## EJEMPLO: CREAR USUARIOS

```kotlin
fun crearUsuario(username: String, password: String, display: String, tipo: String) {
    val usuario = when (tipo.lowercase()) {
        "admin" -> UserAdmin(
            id = "user_${System.currentTimeMillis()}",
            username = username,
            password = password,
            display = display
        )
        "normal" -> UserNormal(
            id = "user_${System.currentTimeMillis()}",
            username = username,
            password = password,
            display = display
        )
        "superadmin" -> UserSuperAdmin(
            id = "user_${System.currentTimeMillis()}",
            username = username,
            password = password,
            display = display
        )
        else -> {
            println("❌ Tipo de usuario desconocido")
            return
        }
    }

    if (usuarioRepository.save(usuario)) {
        println("✅ Usuario creado!")
    } else {
        println("❌ Error al crear usuario")
    }
}

fun listarUsuarios() {
    val usuarios = usuarioRepository.findAll()
    usuarios.forEach { println(it) }
}
```

---

## FLUJO COMPLETO DE PRUEBA

```kotlin
fun main() {
    val gestor = GestorOci()

    println("=== CREANDO CATEGORÍAS ===")
    gestor.crearCategoria("Libros")
    gestor.crearCategoria("Películas")
    gestor.crearCategoria("Videojuegos")

    println("\n=== CREANDO USUARIOS ===")
    gestor.crearUsuario("admin1", "pass123", "Administrador", "admin")
    gestor.crearUsuario("user1", "pass123", "Usuario Normal", "normal")

    println("\n=== CREANDO ELEMENTOS ===")
    gestor.crearElemento("1984", "Novela distópica", "Libros")
    gestor.crearElemento("Dune", "Novela de ciencia ficción", "Libros")
    gestor.crearElemento("Inception", "Película de ciencia ficción", "Películas")

    println("\n=== LISTANDO CATEGORÍAS ===")
    gestor.listarCategorias()

    println("\n=== LISTANDO USUARIOS ===")
    gestor.listarUsuarios()

    println("\n=== LISTANDO ELEMENTOS ===")
    gestor.listarElementos()
}
```

**Resultado esperado:**
```
=== CREANDO CATEGORÍAS ===
✅ Categoría creada!
✅ Categoría creada!
✅ Categoría creada!

=== CREANDO USUARIOS ===
✅ Usuario creado!
✅ Usuario creado!

=== CREANDO ELEMENTOS ===
✅ Elemento creado!
✅ Elemento creado!
✅ Elemento creado!

=== LISTANDO CATEGORÍAS ===
Categoria(nombre=Libros)
Categoria(nombre=Películas)
Categoria(nombre=Videojuegos)

=== LISTANDO USUARIOS ===
UserAdmin(id=user_1712762345, username=admin1, password=pass123, display=Administrador)
UserNormal(id=user_1712762346, username=user1, password=pass123, display=Usuario Normal)

=== LISTANDO ELEMENTOS ===
ElementOci(id=elemento_1712762347, titulo=1984, descripcion=Novela distópica, categoria=Categoria(nombre=Libros))
ElementOci(id=elemento_1712762348, titulo=Dune, descripcion=Novela de ciencia ficción, categoria=Categoria(nombre=Libros))
ElementOci(id=elemento_1712762349, titulo=Inception, descripcion=Película de ciencia ficción, categoria=Categoria(nombre=Películas))
```

---

## ARCHIVOS JSON DESPUÉS DE EJECUTAR

### data/categorias.json
```json
[
  {
    "nombre": "Libros"
  },
  {
    "nombre": "Películas"
  },
  {
    "nombre": "Videojuegos"
  }
]
```

### data/usuarios.json
```json
[
  {
    "id": "user_1712762345",
    "username": "admin1",
    "password": "pass123",
    "display": "Administrador"
  },
  {
    "id": "user_1712762346",
    "username": "user1",
    "password": "pass123",
    "display": "Usuario Normal"
  }
]
```

### data/elementos.json
```json
[
  {
    "id": "elemento_1712762347",
    "titulo": "1984",
    "descripcion": "Novela distópica",
    "categoria": {
      "nombre": "Libros"
    }
  },
  {
    "id": "elemento_1712762348",
    "titulo": "Dune",
    "descripcion": "Novela de ciencia ficción",
    "categoria": {
      "nombre": "Libros"
    }
  },
  {
    "id": "elemento_1712762349",
    "titulo": "Inception",
    "descripcion": "Película de ciencia ficción",
    "categoria": {
      "nombre": "Películas"
    }
  }
]
```

---

## VENTAJAS DE ESTE SISTEMA

✅ **Un JSON por tipo de dato** - Fácil de entender
✅ **Reutilizable** - El mismo `JsonRepository` para todo
✅ **Escalable** - Añadir nuevos tipos es fácil
✅ **Separado** - Cada entidad en su archivo
✅ **Legible** - Los JSONs son claros y simples

---

## CÓMO AÑADIR UN NUEVO REPOSITORIO

Si quieres añadir más entidades (ej: `Estado`):

**1. Crea el modelo:**
```kotlin
@Serializable
data class Estado(val nombre: String)
```

**2. Crea el archivo JSON:**
```bash
# data/estados.json
[]
```

**3. Instancia el repositorio en GestorOci:**
```kotlin
private val estadoRepository: Repository<Estado> = JsonRepository(
    "data/estados.json",
    serializer<Estado>()
)
```

¡Listo! Ya tienes un nuevo repositorio.

---

## SIGUIENTE

👉 Lee: [`04_LISTAS_ANIDADAS.md`](./04_LISTAS_ANIDADAS.md)

Si quieres que cada usuario tenga su **propia lista de elementos** (anidada dentro del usuario), esa guía es para ti.


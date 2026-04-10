# 📚 5. SAVE vs UPDATE - Cuándo Usar Cada Uno

## DIFERENCIA RÁPIDA

| Operación | Cuándo usar | Resultado |
|-----------|------------|-----------|
| **SAVE** | Crear algo **nuevo** | Añade un elemento a la lista |
| **UPDATE** | Modificar algo **existente** | Reemplaza los datos del elemento |

---

## VISUAL: SAVE vs UPDATE

### SAVE (Crear)
```
ARCHIVO JSON (ANTES):
[
  { "id": "1", "titulo": "Libro1", "descripcion": "Desc1" }
]

→ elementoRepository.save(ElementOci("2", "Libro2", "Desc2"))

ARCHIVO JSON (DESPUÉS):
[
  { "id": "1", "titulo": "Libro1", "descripcion": "Desc1" },
  { "id": "2", "titulo": "Libro2", "descripcion": "Desc2" }  ← AÑADIDO
]
```

### UPDATE (Modificar)
```
ARCHIVO JSON (ANTES):
[
  { "id": "1", "titulo": "Libro1", "descripcion": "Desc1" }
]

→ elementoRepository.update(ElementOci("1", "Libro Modificado", "Nueva Desc"))

ARCHIVO JSON (DESPUÉS):
[
  { "id": "1", "titulo": "Libro Modificado", "descripcion": "Nueva Desc" }  ← MODIFICADO
]
```

---

## EN TU PROYECTO: EJEMPLOS REALES

### Caso 1: Crear un nuevo usuario (SAVE)

```kotlin
fun crearUsuario(username: String, password: String, display: String, tipo: String) {
    val nuevoUsuario = UserNormal(
        id = "user_${System.currentTimeMillis()}",  // ← ID nuevo
        username = username,
        password = password,
        display = display,
        elementos = mutableListOf()
    )

    // El usuario NO existe todavía → SAVE
    usuarioRepository.save(nuevoUsuario)
}
```

### Caso 2: Cambiar la contraseña de un usuario (UPDATE)

```kotlin
fun cambiarContraseña(usuarioId: String, nuevaContraseña: String) {
    // 1. Buscamos el usuario existente
    val usuario = usuarioRepository.findById(usuarioId)

    if (usuario != null) {
        // 2. Creamos una copia con la contraseña nueva
        val usuarioActualizado = usuario.copy(
            password = nuevaContraseña
        )

        // El usuario YA existe → UPDATE
        usuarioRepository.update(usuarioActualizado)
    }
}
```

### Caso 3: Añadir un elemento a un usuario (UPDATE del usuario)

```kotlin
fun agregarElementoAUsuario(usuarioId: String, titulo: String, descripcion: String, categoriaNombre: String) {
    // 1. Buscamos el usuario
    val usuario = usuarioRepository.findById(usuarioId)
    if (usuario == null) return

    // 2. Creamos el nuevo elemento
    val nuevoElemento = ElementOci(
        id = "elem_${System.currentTimeMillis()}",
        titulo = titulo,
        descripcion = descripcion,
        categoria = Categoria(categoriaNombre)
    )

    // 3. Lo añadimos a la lista del usuario
    usuario.elementos.add(nuevoElemento)

    // 4. El usuario YA existe, solo cambiamos su lista → UPDATE
    usuarioRepository.update(usuario)
}
```

### Caso 4: Editar un elemento dentro de un usuario (UPDATE del usuario)

```kotlin
fun editarElementoDelUsuario(usuarioId: String, elementoId: String, nuevoTitulo: String) {
    // 1. Buscamos el usuario
    val usuario = usuarioRepository.findById(usuarioId)
    if (usuario == null) return

    // 2. Buscamos el elemento dentro de su lista
    val index = usuario.elementos.indexOfFirst { it.id == elementoId }
    if (index == -1) return

    // 3. Creamos una copia modificada del elemento
    val elementoModificado = usuario.elementos[index].copy(titulo = nuevoTitulo)

    // 4. Reemplazamos en la lista
    usuario.elementos[index] = elementoModificado

    // 5. El usuario YA existe, solo cambia su lista → UPDATE
    usuarioRepository.update(usuario)
}
```

### Caso 5: Eliminar un elemento de un usuario (UPDATE del usuario)

```kotlin
fun eliminarElementoDelUsuario(usuarioId: String, elementoId: String) {
    // 1. Buscamos el usuario
    val usuario = usuarioRepository.findById(usuarioId)
    if (usuario == null) return

    // 2. Eliminamos el elemento de su lista
    usuario.elementos.removeIf { it.id == elementoId }

    // 3. El usuario YA existe, solo cambia su lista → UPDATE
    usuarioRepository.update(usuario)
}
```

---

## RESUMEN: CUÁNDO USAR CADA UNO

### SAVE: Crear Nuevas Entidades
```kotlin
// ✅ SAVE para crear un nuevo usuario
usuarioRepository.save(UserNormal(...))

// ✅ SAVE para crear una nueva categoría
categoriaRepository.save(Categoria(...))

// ✅ SAVE para crear un nuevo elemento (si está fuera del usuario)
elementoRepository.save(ElementOci(...))
```

### UPDATE: Modificar Entidades Existentes
```kotlin
// ✅ UPDATE para cambiar datos del usuario
usuarioRepository.update(usuarioModificado)

// ✅ UPDATE para añadir/quitar/editar elementos del usuario
usuarioRepository.update(usuarioConElementosModificados)

// ✅ UPDATE para cambiar una categoría existente
categoriaRepository.update(categoriaModificada)
```

---

## IMPORTANTE: Con Listas Anidadas

Cuando trabajas con listas anidadas (usuario con elementos):

```
┌─── SAVE (usuario nuevo) ────────────┐
│  usuarioRepository.save(nuevoUsuario)  │
└────────────────────────────────────┘

┌─── UPDATE (usuario + elementos) ───┐
│  usuario.elementos.add(nuevoElemento) │
│  usuarioRepository.update(usuario)    │
└────────────────────────────────────┘
```

**Nota:** El **usuario** es SAVE o UPDATE según sea nuevo o existente.
Pero los **elementos dentro del usuario** se manejan modificando su lista y luego UPDATE del usuario.

---

## FLUJO LÓGICO PARA DECIDIR

```
¿El ID del objeto ya existe en el JSON?

    ├─ SÍ  → UPDATE (modificar los datos existentes)
    └─ NO  → SAVE (crear uno nuevo)
```

---

## ERRORES COMUNES

### ❌ Error 1: Usar SAVE cuando debería ser UPDATE

```kotlin
// INCORRECTO ❌
val usuario = usuarioRepository.findById("123")
usuario.password = "nueva_contraseña"
usuarioRepository.save(usuario)  // ← Crea OTRO usuario con el mismo ID

// CORRECTO ✅
val usuario = usuarioRepository.findById("123")
val usuarioActualizado = usuario.copy(password = "nueva_contraseña")
usuarioRepository.update(usuarioActualizado)  // ← Modifica el existente
```

### ❌ Error 2: Usar UPDATE cuando debería ser SAVE

```kotlin
// INCORRECTO ❌
val usuarioNuevo = UserNormal("", "juan", "pass", "Juan")
usuarioRepository.update(usuarioNuevo)  // ← No existe el ID, va a fallar

// CORRECTO ✅
val usuarioNuevo = UserNormal("user_${System.currentTimeMillis()}", "juan", "pass", "Juan")
usuarioRepository.save(usuarioNuevo)  // ← Crea uno nuevo
```

### ❌ Error 3: Olvidar UPDATE después de modificar una lista anidada

```kotlin
// INCORRECTO ❌
val usuario = usuarioRepository.findById("123")
usuario.elementos.add(nuevoElemento)
// ← Cambio en memoria, pero NO guardado en JSON

// CORRECTO ✅
val usuario = usuarioRepository.findById("123")
usuario.elementos.add(nuevoElemento)
usuarioRepository.update(usuario)  // ← GUARDAR los cambios
```

---

## CHECKLIST FINAL

Antes de usar SAVE o UPDATE, pregúntate:

- [ ] ¿El objeto ya existe en el JSON? → UPDATE
- [ ] ¿Es un objeto nuevo? → SAVE
- [ ] ¿He modificado una lista anidada? → UPDATE del padre
- [ ] ¿He guardado los cambios? → ¿He llamado a save() o update()?

---

## SIGUIENTE

👉 Lee: [`06_TROUBLESHOOTING.md`](./06_TROUBLESHOOTING.md)

Si algo no funciona, esa guía tiene las soluciones a problemas comunes.


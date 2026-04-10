# 📚 Guías de Implementación - Índice

Bienvenido a las guías de implementación del patrón Repository en el proyecto **llista-oci**.

## 📖 Guías Disponibles

### 1. **Concepto Básico del Repository**
📄 [`01_REPOSITORY_BASICO.md`](./01_REPOSITORY_BASICO.md)
- ¿Qué es el patrón Repository?
- Arquitectura básica
- Flujo de datos
- Ventajas y desventajas

### 2. **Implementación Paso a Paso**
📄 [`02_IMPLEMENTACION_PASO_A_PASO.md`](./02_IMPLEMENTACION_PASO_A_PASO.md)
- Cómo instalar dependencias
- Crear la interfaz Repository
- Crear JsonRepository
- Añadir @Serializable a modelos
- Crear instancias

### 3. **Múltiples JSON por Entidad** ⭐ (Especial para ti)
📄 [`03_MULTIPLES_REPOSITORIES.md`](./03_MULTIPLES_REPOSITORIES.md)
- Un JSON por cada clase (elementos, usuarios, categorías)
- Estructura de carpetas `data/`
- Configuración de cada repositorio
- Ejemplo práctico en tu proyecto

### 4. **Anidar Listas: Usuarios con sus Elementos**
📄 [`04_LISTAS_ANIDADAS.md`](./04_LISTAS_ANIDADAS.md)
- Cómo cada usuario tenga su propia lista
- Modelos anidados con @Serializable
- Operaciones CRUD con listas dentro de listas
- JSON con estructura jerárquica
- Ejemplo práctico

### 5. **Diferencia entre SAVE y UPDATE**
📄 [`05_SAVE_vs_UPDATE.md`](./05_SAVE_vs_UPDATE.md)
- ¿Cuándo usar cada uno?
- Diferencias visuales
- Casos de uso reales
- Cómo implementarlos

### 6. **Troubleshooting y Problemas Comunes**
📄 [`06_TROUBLESHOOTING.md`](./06_TROUBLESHOOTING.md)
- Errores de compilación
- Problemas de serialización
- Errores de paths
- Cómo debuggear

---

## 🎯 Flujo de Aprendizaje Recomendado

```
1. Lee 01_REPOSITORY_BASICO.md
   ↓
2. Lee 02_IMPLEMENTACION_PASO_A_PASO.md
   ↓
3. Implementa las clases Repository y JsonRepository
   ↓
4. Lee 03_MULTIPLES_REPOSITORIES.md (tu caso)
   ↓
5. Crea los JSONs para cada entidad
   ↓
6. Lee 04_LISTAS_ANIDADAS.md
   ↓
7. Estructura usuarios con sus listas propias
   ↓
8. ¡Haz pruebas y refiere a 06_TROUBLESHOOTING.md si algo falla!
```

---

## 🗂️ Estructura de Carpetas después de implementar

```
llista-oci/
├── docs/                          ← TÚ ESTÁS AQUÍ
│   ├── 00_INDICE.md              (esta guía)
│   ├── 01_REPOSITORY_BASICO.md
│   ├── 02_IMPLEMENTACION_PASO_A_PASO.md
│   ├── 03_MULTIPLES_REPOSITORIES.md
│   ├── 04_LISTAS_ANIDADAS.md
│   ├── 05_SAVE_vs_UPDATE.md
│   └── 06_TROUBLESHOOTING.md
├── data/                          ← ARCHIVOS JSON
│   ├── elementos.json
│   ├── categorias.json
│   └── usuarios.json
├── src/main/kotlin/
│   ├── repository/                ← PATRÓN
│   │   ├── Repository.kt
│   │   └── JsonRepository.kt
│   ├── models/                    ← TUS MODELOS
│   │   ├── User.kt
│   │   ├── ElementOci.kt
│   │   ├── Categoria.kt
│   │   └── ...
│   ├── app/
│   │   └── GestorOci.kt          ← USA LOS REPOSITORIES
│   └── Main.kt
└── ...
```

---

## ⚡ Quick Start (para los impacientes)

Si ya sabes qué hacer:

1. **Copia estos archivos:**
   - [`02_IMPLEMENTACION_PASO_A_PASO.md`](./02_IMPLEMENTACION_PASO_A_PASO.md)
   - [`03_MULTIPLES_REPOSITORIES.md`](./03_MULTIPLES_REPOSITORIES.md)

2. **Implementa:**
   - `src/main/kotlin/repository/Repository.kt`
   - `src/main/kotlin/repository/JsonRepository.kt`

3. **Configura los modelos:**
   - Añade `@Serializable` a User, ElementOci, Categoria, etc.

4. **Crea los JSONs en `data/`:**
   - elementos.json (array vacío)
   - categorias.json (array vacío)
   - usuarios.json (array vacío)

5. **En GestorOci.kt:**
   ```kotlin
   private val elementoRepository = JsonRepository("data/elementos.json", serializer<ElementOci>())
   private val categoriaRepository = JsonRepository("data/categorias.json", serializer<Categoria>())
   private val usuarioRepository = JsonRepository("data/usuarios.json", serializer<User>())
   ```

¡Listo! 🚀

---

## 💡 Preguntas Frecuentes

**P: ¿Necesito una interfaz Repository?**
R: Sí, es el contrato. Define qué operaciones se pueden hacer.

**P: ¿Un JSON por entidad o todo en uno?**
R: Depende, pero para tu caso (un JSON por clase), mira [`03_MULTIPLES_REPOSITORIES.md`](./03_MULTIPLES_REPOSITORIES.md)

**P: ¿Cómo anido listas dentro de usuarios?**
R: Lee [`04_LISTAS_ANIDADAS.md`](./04_LISTAS_ANIDADAS.md)

**P: ¿Cuándo uso SAVE vs UPDATE?**
R: Lee [`05_SAVE_vs_UPDATE.md`](./05_SAVE_vs_UPDATE.md)

---

## 📞 Necesitas Ayuda?

Si algo no está claro:
1. Revisa [`06_TROUBLESHOOTING.md`](./06_TROUBLESHOOTING.md)
2. Busca tu error en las guías
3. Pregunta al asistente (me!)

---

**Última actualización:** 2026-04-10
**Kotlin:** 2.3.0
**Serialización:** kotlinx-serialization-json 1.6.0


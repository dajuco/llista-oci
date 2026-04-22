# Ejemplo basico de Compose + MVVM

Mini proyecto generico para estudiar este flujo:

`UI -> ViewModel -> Repository -> JSON`

No esta adaptado a ningun dominio concreto. La idea es que lo uses como plantilla didactica.

## Estructura

- `src/main/kotlin/ui`: Composables (sin logica de negocio)
- `src/main/kotlin/viewmodel`: estado y eventos
- `src/main/kotlin/repository`: acceso a datos JSON
- `src/main/kotlin/model`: entidades
- `data/items.json`: archivo persistido en runtime
- `src/main/resources/data/seed_items.json`: datos semilla

## Que incluye

- Lista de items
- Formulario para crear item
- Cambio de estado (ciclo pendiente -> en proceso -> completado)
- Eliminacion
- Validaciones basicas
- Mensajes de error y confirmacion

## Ejecutar

Desde la carpeta raiz del workspace:

```bash
cd /home/super/IdeaProjects/llista-oci/ejemplo
../gradlew run
```

Tambien puedes probar build y tests:

```bash
cd /home/super/IdeaProjects/llista-oci/ejemplo
../gradlew build
```

## Notas

- El archivo `data/items.json` se crea automaticamente en el primer arranque.
- Si quieres reiniciar datos, borra `data/items.json`.
- Este ejemplo usa Compose Desktop para simplificar el aprendizaje en IntelliJ IDEA.


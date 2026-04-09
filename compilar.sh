#!/bin/bash

# 1. Compilar el proyecto (Generar el JAR nuevo)
echo "🔨 Compilando nueva versión..."
./gradlew jar

# 2. Definir rutas (Ajusta esto a donde tengas tu JAR "final")
ORIGEN="build/libs/llista-oci-1.0-SNAPSHOT.jar"
DESTINO="/home/super/llista-oci-1.0-all.jar"

# 3. Mover y avisar
if [ -f "$ORIGEN" ]; then
    cp "$ORIGEN" "$DESTINO"
    echo "✅ Software actualizado y listo para el cliente en $DESTINO"
else
    echo "❌ Error: No se pudo generar el JAR."
fi
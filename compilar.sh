#!/bin/bash

echo "🔨 Compilando nueva versión..."
./gradlew jar

ORIGEN="build/libs/llista-oci-1.0-SNAPSHOT.jar"
DESTINO="/home/super/llista-oci-1.0-all.jar"

if [ -f "$ORIGEN" ]; then
    cp "$ORIGEN" "$DESTINO"
    echo "✅ Software actualizado y listo para el cliente en $DESTINO"
else
    echo "❌ Error: No se pudo generar el JAR."
fi
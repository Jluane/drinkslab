#!/bin/bash

echo "=== Configuración del lector de códigos de barras ==="
echo

USUARIO="${SUDO_USER:-$USER}"

echo "Conecta el lector de códigos de barras."
echo "Buscando dispositivos seriales..."

DISPOSITIVOS=$(find /dev -maxdepth 1 \( -name 'ttyACM*' -o -name 'ttyUSB*' \) 2>/dev/null)

if [ -z "$DISPOSITIVOS" ]; then
    echo "No se encontró ningún lector."
    echo "Comprueba que esté conectado y vuelve a ejecutar."
    exit 1
fi

echo
echo "Dispositivos encontrados:"
echo "$DISPOSITIVOS"
echo

DISPOSITIVO=$(echo "$DISPOSITIVOS" | head -n 1)
GRUPO=$(stat -c '%G' "$DISPOSITIVO")

echo "Dispositivo seleccionado: $DISPOSITIVO"
echo "Grupo de permisos: $GRUPO"

if [ "$GRUPO" = "root" ]; then
    echo "El dispositivo pertenece a root."
    echo "Es necesario revisar sus permisos antes de continuar."
    exit 1
fi

sudo usermod -aG "$GRUPO" "$USUARIO"

echo
echo "Configuración realizada."
echo "Usuario agregado al grupo: $GRUPO"
echo
echo "Cierra tu sesión y vuelve a entrar para aplicar los cambios."

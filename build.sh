#!/bin/bash

set -e

cd lib

echo "🔨 Compilando para Linux (.so)..."
cargo build --release

echo "🔨 Compilando para Windows (.dll)..."
rustup target add x86_64-pc-windows-gnu
cargo build --release --target x86_64-pc-windows-gnu

cd ..

# Cria diretórios de saída
mkdir -p package/src/main/resources/native/linux
mkdir -p package/src/main/resources/native/windows

# Copia os binários compilados
cp lib/target/release/libyt_downloader_lib.so package/src/main/resources/native/linux/
cp lib/target/x86_64-pc-windows-gnu/release/yt_downloader_lib.dll package/src/main/resources/native/windows/

echo "✅ Bibliotecas copiada com sucesso:"
echo " - Linux:   package/src/main/resources/native/linux/libyt_downloader_lib.so"
echo " - Windows: package/src/main/resources/native/windows/yt_downloader_lib.dll"

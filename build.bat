@echo off
setlocal enableextensions

echo 🔨 Compilando para Windows (.dll)...

REM Navega até a pasta do projeto Rust
cd lib

REM Compila para Windows (x86_64-pc-windows-gnu)
rustup target add x86_64-pc-windows-gnu
cargo build --release --target x86_64-pc-windows-gnu

REM Volta para a raiz
cd ..

REM Cria diretórios de saída, se não existirem
if not exist "package\src\main\resources\native\windows" mkdir "package\src\main\resources\native\windows"

REM Copia a DLL para a pasta resources
copy /Y lib\target\x86_64-pc-windows-gnu\release\yt_downloader_lib.dll package\src\main\resources\native\windows\

echo ✅ DLL compilada e copiada com sucesso!
echo  - Windows: package\src\main\resources\native\windows\yt_downloader_lib.dll

endlocal
#!/bin/bash

# compila a lib Rust
cd lib
cargo build --release

cd ..
mkdir -p package/src/main/resources/native/linux
cp lib/target/release/libyt_downloader_lib.so package/src/main/resources/native/linux/

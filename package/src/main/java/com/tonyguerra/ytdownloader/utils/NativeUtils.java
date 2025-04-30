package com.tonyguerra.ytdownloader.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

public final class NativeUtils {
    public static void loadLibraryFromResources(String libName) {
        try {
            final String os = System.getProperty("os.name").toLowerCase();
            String libFile = "";

            if (os.contains("linux")) {
                libFile = "/native/linux/lib" + libName + ".so";
            } else if (os.contains("windows")) {
                libFile = "/native/windows/" + libName + ".dll";
            } else if (os.contains("mac")) {
                libFile = "/native/macos/lib" + libName + ".dylib";
            } else {
                throw new UnsupportedOperationException("OS não suportado");
            }

            final var in = NativeUtils.class.getResourceAsStream(libFile);
            if (in == null)
                throw new RuntimeException("Biblioteca não encontrada: " + libFile);

            File tempFile = Files.createTempFile("lib", libName).toFile();
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                in.transferTo(out);
            }

            System.load(tempFile.getAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar biblioteca nativa", e);
        }
    }
}

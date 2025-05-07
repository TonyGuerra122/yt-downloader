package com.tonyguerra.ytdownloader.utils;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class NativeUtils {
    public static void loadLibraryFromResources(String libName) {
        try {
            final String os = System.getProperty("os.name").toLowerCase();
            String libFile;

            if (os.contains("linux")) {
                libFile = "/native/linux/lib" + libName + ".so";
            } else if (os.contains("windows")) {
                libFile = "/native/windows/" + libName + ".dll";
            } else if (os.contains("mac")) {
                libFile = "/native/macos/lib" + libName + ".dylib";
            } else {
                throw new UnsupportedOperationException("Sistema operacional não suportado: " + os);
            }

            final var in = NativeUtils.class.getResourceAsStream(libFile);
            if (in == null)
                throw new RuntimeException("Biblioteca nativa não encontrada: " + libFile);

            final var tempDir = Files.createTempDirectory("ytlibs");
            tempDir.toFile().deleteOnExit();

            final var extractedFile = tempDir.resolve(Paths.get(libFile).getFileName()).toFile();
            extractedFile.deleteOnExit();

            try (final var out = new FileOutputStream(extractedFile)) {
                in.transferTo(out);
            }

            System.load(extractedFile.getAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar biblioteca nativa", e);
        }
    }
}

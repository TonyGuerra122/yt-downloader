package com.tonyguerra.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

import com.tonyguerra.errors.UnsupportedOperationalSystemException;

/**
 * Classe de utilitário do sistema operacional
 * 
 * @author Tony Guerra
 */
public final class SystemUtils {
    private static final List<String> SUPPORTED_OS = List.of("win", "mac", "nix", "nux", "aix");

    private static void checkOsCompatibility() throws UnsupportedOperationalSystemException {
        final String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (!SUPPORTED_OS.stream().anyMatch(os::contains))
            throw new UnsupportedOperationalSystemException("Sistema operacional não suportado");
    }

    public static Path getDownloaderFolder() throws UnsupportedOperationalSystemException {
        checkOsCompatibility();

        return Paths.get(System.getProperty("user.home"), "Downloads");
    }

    /**
     * Método que valida se o Youtube-DL está instalado
     */
    public static boolean isYoutubeDLInstalled() {
        try {
            final var process = new ProcessBuilder("youtube-dl", "--version").start();
            try (final var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                final String version = reader.readLine();
                return version != null && !version.isEmpty();
            }
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Método responsável por installar o youtube-dl
     * 
     * @throws Exception se falhar
     */
    public static void installYoutubeDL() throws Exception {
        checkOsCompatibility();
        final String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        if (os.contains("win")) {
            final String url = "https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp.exe";
            final String outputPath = System.getProperty("user.home") + "\\yt-dlp.exe";
            downloadFile(url, outputPath);
            System.out.println("yt-dlp instalado em: " + outputPath);
        } else {
            final String downloadCommand = "sudo curl -L https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -o /usr/local/bin/youtube-dl";
            final String permissionCommand = "sudo chmod a+rx /usr/local/bin/youtube-dl";

            executeCommand(downloadCommand);
            executeCommand(permissionCommand);

            System.out.println("yt-dlp instalado em: /usr/local/bin/youtube-dl");
        }
    }

    /**
     * Faz o download de um arquivo do URL especificado
     *
     * @param url        URL do arquivo
     * @param outputPath Caminho de saída
     * @throws Exception se falhar
     */
    private static void downloadFile(String url, String outputPath) throws Exception {
        final var process = new ProcessBuilder("curl", "-L", url, "-o", outputPath).start();
        final int exitCode = process.waitFor();

        final var path = Paths.get(outputPath);

        if (exitCode != 0 || !Files.exists(path)) {
            throw new Exception("Falha no download do arquivo: " + url);
        }
    }

    private static void executeCommand(String command) throws Exception {
        final var process = new ProcessBuilder("bash", "-c", command).start();

        try (final var errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            final var outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while ((line = outputReader.readLine()) != null) {
                System.out.println(line);
            }

            while ((line = errorReader.readLine()) != null) {
                System.err.println("Erro: " + line);
            }
        }

        final int exitCode = process.waitFor();
        if (exitCode != 0)
            throw new Exception("Falha ao executar comando: " + command);
    }
}

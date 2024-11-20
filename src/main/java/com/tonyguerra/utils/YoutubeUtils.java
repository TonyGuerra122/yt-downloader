package com.tonyguerra.utils;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.tonyguerra.enums.MediaType;

/**
 * Classe para utilitário do Youtube
 * 
 * @author Tony Guerra
 */
public final class YoutubeUtils {

    /**
     * Método utilizado para instalar o vídeo/áudio do Youtube pela URL
     * 
     * @param videoUrl  URL do vídeo
     * @param mediaType Tipo da mídia (AUDIO ou VIDEO)
     * @throws Exception Em caso de falha
     */
    public static void installMedia(String videoUrl, MediaType mediaType) throws Exception {
        if (!SystemUtils.isYoutubeDLInstalled()) {
            SystemUtils.installYoutubeDL();
        }

        final var request = new YoutubeDLRequest(videoUrl, SystemUtils.getDownloaderFolder().toString());
        request.setOption("output", "%(title)s.%(ext)s");

        if (mediaType == null) {
            throw new IllegalArgumentException("O MediaType está inválido");
        }

        request.setOption("format", mediaType.getMediaFormat());

        try {
            System.out.println("Iniciando download para URL: " + videoUrl);
            final var response = YoutubeDL.execute(request);
            System.out.println("Download concluído com sucesso!");
            System.out.println(response.getOut());
        } catch (Exception e) {
            System.err.println("Erro ao baixar mídia da URL: " + videoUrl);
            throw e;
        }
    }
}

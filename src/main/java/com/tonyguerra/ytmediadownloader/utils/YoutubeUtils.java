package com.tonyguerra.ytmediadownloader.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.tonyguerra.ytmediadownloader.dto.VideoInfoDTO;
import com.tonyguerra.ytmediadownloader.enums.MediaType;

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

    /**
     * Método utilizado para obter informações do vídeo
     * 
     * @param url URL do vídeo
     * @return Informações do vídeo
     * @throws Exception
     */
    public static VideoInfoDTO getVideoInfo(String url) throws Exception {
        if (!SystemUtils.isYoutubeDLInstalled()) {
            SystemUtils.installYoutubeDL();
        }

        final var request = new YoutubeDLRequest(url);
        request.setOption("dump-json");

        try {
            System.out.println("Obtendo informações do vídeo: " + url);
            final var response = YoutubeDL.execute(request);
            System.out.println("Informações obtidas com sucesso!");

            final var videoInfo = new ObjectMapper().readValue(response.getOut(), VideoInfoDTO.class);

            return videoInfo;

        } catch (Exception ex) {
            System.err.println("Erro ao obter informações do vídeo: " + url);
            throw ex;
        }
    }
}

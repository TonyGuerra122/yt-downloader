# YoutubeDownloader
Uma biblioteca Java simples para baixar vídeos e áudios do YouTube com suporte a formatos de alta qualidade. Ela utiliza a biblioteca youtube-dl (ou yt-dlp) como backend e oferece uma interface para configurar e gerenciar downloads.
Recursos
-   Baixe vídeos em alta qualidade, com áudio mesclado automaticamente.
-   Extraia o áudio de vídeos em formatos como MP3, AAC ou WAV.
-   Suporte a escolha de formatos específicos para áudio e vídeo.
-   Instalação automática do youtube-dl/yt-dlp e validação de dependências.
-   Escolha dinâmica de pastas para salvar os downloads.

## Requisitos
-   Java 17+
-   youtube-dl ou yt-dlp (instalado automaticamente pela biblioteca).
-   ffmpeg (necessário para conversão de formatos ou mesclagem de vídeo e áudio).

## Instalação
A biblioteca verifica e instala automaticamente o youtube-dl ou yt-dlp. Certifique-se de que o sistema suporta os comandos curl e chmod para permitir a instalação.

## Como Usar
### Baixar um Vídeo
```java
import com.tonyguerra122.ytmediadownloader.utils.YoutubeUtils;
import com.tonyguerra122.ytmediadownloader.enums.MediaType;

public class Main {
    public static void main(String[] args) {
        try {
            // O MediaType deve ser AUDIO se quiser baixar um audio.
            YoutubeUtils.installMedia("https://www.youtube.com/watch?v=EXEMPLO", MediaType.VIDEO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Obter informações de um vídeo
```java
import com.tonyguerra.ytmediadownloader.utils.YoutubeUtils;

public class Main {
    public static void main(String[] args) throws Exception {
        final var info = YoutubeUtils.getVideoInfo("https://www.youtube.com/watch?v=jU27QehicQQ");

        System.out.println("Título: " + info.title());
        System.out.println("Thumbnail: " + info.thumbnail());
        System.out.println("Autor: " + info.author());
        System.out.println("Descrição: " + info.description());
        System.out.println("Duração: " + info.duration());
        System.out.println("Resolução: " + info.resolution());
    }
}
```

## Personalizar Diretório de Downloads
A biblioteca salva os arquivos baixados no diretório padrão definido pelo sistema `(SystemUtils.getDownloaderFolder())`. Para modificar o diretório, ajuste a implementação desse método ou passe uma pasta personalizada no `YoutubeDLRequest`.

## Instalação
Para instalar esta biblioteca você deve colocar o seguinte código no seu pom.xml
```xml
<dependencies>
    <dependency>
        <groupId>com.github.TonyGuerra122.ytmediadownloader</groupId>
        <artifactId>yt-downloader</artifactId>
        <version>1.0.2</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
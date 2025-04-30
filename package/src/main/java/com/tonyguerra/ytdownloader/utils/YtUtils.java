package com.tonyguerra.ytdownloader.utils;

import com.tonyguerra.ytdownloader.dto.Video;

public final class YtUtils {
    static {
        NativeUtils.loadLibraryFromResources("yt_downloader_lib");
    }

    public static native Video getVideo(String url);

    public static native void downloadVideo(Video video, String outpathDir);
}

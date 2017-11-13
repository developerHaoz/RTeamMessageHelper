package com.hans.alpha.network.manager;

/**
 * Created by dzc on 15/11/21.
 */
public interface DownloadListener {
    void onPrepare(long progress, long total);

    void onStart();

    void onDownloading(long progress, long total);

    void onPause();

    void onCancel();

    void onCompleted();

    void onError(Exception e);

    int DOWNLOAD_STATUS_COMPLETED = 1;
    int DOWNLOAD_STATUS_UNCOMPLETED = 2;
    int DOWNLOAD_STATUS_DOWNLOADING = 0;
    int DOWNLOAD_STATUS_PAUSE = 3;
    int DOWNLOAD_STATUS_CANCEL = 4;
    int DOWNLOAD_STATUS_ERROR = 5;
}

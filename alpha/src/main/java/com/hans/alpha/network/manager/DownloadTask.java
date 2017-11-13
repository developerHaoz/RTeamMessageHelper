package com.hans.alpha.network.manager;

import com.hans.alpha.android.Log;
import com.hans.alpha.constant.MotherShipConst;
import com.hans.alpha.dao.DownloadRecordDao;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by 随大大便 on 2017/5/1.
 */

public class DownloadTask {
    private static final String TAG = "MotherShip.DownLoadTask";
    private String url;
    private String savePath;
    private String fileName;

    private AtomicLong completedSize;
    private int downloadStatus;
    private long totalSize;
    private File file;
    private DownloadListener listener;
    private DownloadListener interListener = new DownloadListener() {
        @Override
        public void onPrepare(long progress, long total) {

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onDownloading(long progress, long total) {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Exception e) {

        }
    };
    private long[] startPos = new long[MotherShipConst.DOWNLOAD_CONST.DEFAULT_THREAD_NUM];
    private long[] endPos = new long[MotherShipConst.DOWNLOAD_CONST.DEFAULT_THREAD_NUM];
    private long[] subCompletedSize = new long[MotherShipConst.DOWNLOAD_CONST.DEFAULT_THREAD_NUM];
    private long[] subContentLength = new long[MotherShipConst.DOWNLOAD_CONST.DEFAULT_THREAD_NUM];

    private DownloadTask() {
    }

    public DownloadTask(String url, String savePath, String fileName, DownloadListener listener) {
        this.url = url;
        this.savePath = savePath;
        this.listener = listener;
        this.fileName = fileName;
        this.downloadStatus = DownloadListener.DOWNLOAD_STATUS_UNCOMPLETED;
        this.completedSize = new AtomicLong();
    }

    public DownloadListener getListener() {
        return listener == null ? interListener : listener;
    }

    public long getStartPos(int threadId) {
        return startPos[threadId];
    }

    public long getEndPos(int threadId) {
        return endPos[threadId];
    }

    public long getSubContentLength(int threadId) {
        return subContentLength[threadId];
    }

    public void setStartPos(int i, long startPos) {
        this.startPos[i] = startPos;
    }

    public void setEndPos(int i, long endPos) {
        this.endPos[i] = endPos;
    }

    public void setSubContentLength(int i, long pSubContentLength) {
        this.subContentLength[i] = pSubContentLength;
    }

    public void setSubCompletedSize(int i, long subCompletedSize) {
        this.subCompletedSize[i] = subCompletedSize;
    }

    public File getFile() {
        return file;
    }


    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCompletedSize() {
        return completedSize.get();
    }

    public long addCompletedSize(long add) {
        return completedSize.addAndGet(add);
    }

    public void setCompletedSize(long completedSize) {
        this.completedSize.set(completedSize);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void initialize(long contentLength, DownloadRecordDao downloadRecordDao) {
        assignLengthByThread(contentLength);
        List<DownloadRecord> list = queryByUrl(getUrl(), downloadRecordDao);
        if (list.isEmpty()) {
            isNewDownload = true;
        } else if (list.size() != MotherShipConst.DOWNLOAD_CONST.DEFAULT_THREAD_NUM) {
            Log.e(TAG, "unknow error!!");
            deleteAllRecordByrUrl(downloadRecordDao);
            isNewDownload = true;
        } else {
            isNewDownload = false;
        }

        long preLength = 0;
        for (int i = 0; i < MotherShipConst.DOWNLOAD_CONST.DEFAULT_THREAD_NUM; i++) {
            long startPos = 0L;
            long endPos = 0L;
            startPos = preLength;
            endPos = preLength + subContentLength[i] - 1;
            preLength += subContentLength[i];
            if (isNewDownload) {
                insertRecord(downloadRecordDao, i);
            } else {
                startPos += list.get(i).getCompletedSize();
                subContentLength[i] = endPos - startPos + 1;
            }
            setStartPos(i, startPos);
            setEndPos(i, endPos);
        }
    }

    private void assignLengthByThread(long contentLength) {
        long temp = contentLength / MotherShipConst.DOWNLOAD_CONST.DEFAULT_THREAD_NUM;
        for (int i = 0; i < MotherShipConst.DOWNLOAD_CONST.DEFAULT_THREAD_NUM; i++) {
            subContentLength[i] = temp;
            if (i == MotherShipConst.DOWNLOAD_CONST.DEFAULT_THREAD_NUM - 1) {
                subContentLength[i] += contentLength % temp;
            }
            setSubContentLength(i, subContentLength[i]);
            Log.d(TAG, subContentLength[i]);
        }
    }

    boolean isNewDownload;

    public boolean isNewTask() {
        return getDownloadStatus() == DownloadListener.DOWNLOAD_STATUS_UNCOMPLETED && isNewDownload;
    }

    private void insertRecord(DownloadRecordDao downloadRecordDao, int threadId) {
        DownloadRecord record = new DownloadRecord();
        record.setCompletedSize(0);
        record.setTotalSize(subContentLength[threadId]);
        record.setFileName(getFileName());
        record.setDownloadingFileName(getDownloadingName());
        record.setSavePath(getSavePath());
        record.setUrl(getUrl());
        record.setThreadId(threadId);
        downloadRecordDao.insert(record);
    }

    private List<DownloadRecord> queryByUrl(String url, DownloadRecordDao downloadRecordDao) {
        return downloadRecordDao.queryBuilder().where(DownloadRecordDao.Properties.Url.eq(url)).list();
    }

    public String getDownloadingName() {
        String endString = ".fd";
        String startString = getFileName().substring(0, getFileName().lastIndexOf(".") - 1);
        return startString + endString;
    }

    public void deleteAllRecordByrUrl(DownloadRecordDao downloadRecordDao) {
        downloadRecordDao.queryBuilder()
                .where(DownloadRecordDao.Properties.Url.eq(getUrl()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
        downloadRecordDao.detachAll();
    }
}

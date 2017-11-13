package com.hans.alpha.network.manager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by 随大大便 on 2017/4/30.
 */
@Entity(
        indexes = {@Index(value = "url,threadId", unique = true)}
)
public class DownloadRecord {
    @Id(autoincrement = true)
    private Long _id;
    @Property(nameInDb = "threadId")
    private int threadId;
    private long totalSize;
    private long completedSize;
    @NotNull
    @Property(nameInDb = "url")
    private String url;
    @NotNull
    private String savePath;
    @NotNull
    private String fileName;
    @NotNull
    private String downloadingFileName;
    private int downloadStatus;

    @Generated(hash = 995691799)
    public DownloadRecord(Long _id, int threadId, long totalSize, long completedSize,
            @NotNull String url, @NotNull String savePath, @NotNull String fileName,
            @NotNull String downloadingFileName, int downloadStatus) {
        this._id = _id;
        this.threadId = threadId;
        this.totalSize = totalSize;
        this.completedSize = completedSize;
        this.url = url;
        this.savePath = savePath;
        this.fileName = fileName;
        this.downloadingFileName = downloadingFileName;
        this.downloadStatus = downloadStatus;
    }

    @Generated(hash = 155491740)
    public DownloadRecord() {
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCompletedSize() {
        return completedSize;
    }

    public void setCompletedSize(long completedSize) {
        this.completedSize = completedSize;
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

    public String getDownloadingFileName() {
        return downloadingFileName;
    }

    public void setDownloadingFileName(String downloadingFileName) {
        this.downloadingFileName = downloadingFileName;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }
}

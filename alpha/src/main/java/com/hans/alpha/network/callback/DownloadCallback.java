package com.hans.alpha.network.callback;

import com.hans.alpha.android.Log;
import com.hans.alpha.dao.DownloadRecordDao;
import com.hans.alpha.network.manager.DownloadListener;
import com.hans.alpha.network.manager.DownloadManager;
import com.hans.alpha.network.manager.DownloadRecord;
import com.hans.alpha.network.manager.DownloadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by changelcai on 2016/3/18.
 */
public class DownloadCallback extends SceneCallback<RandomAccessFile> {

    private static final String TAG = "MotherShip.DownloadCallback";

    /**
     * 目标文件存储的文件
     */
    private File file;

    DownloadRecordDao downloadRecordDao;
    private int threadId;
    DownloadTask task;

    public DownloadCallback(DownloadTask task, int threadId, DownloadRecordDao downloadRecordDao) {
        this.task = task;
        this.file = task.getFile();
        this.threadId = threadId;
        this.downloadRecordDao = downloadRecordDao;
    }

    @Override
    public RandomAccessFile parseNetworkResponse(Response response) {
        return saveFile(response);
    }

    @Override
    public int getHandleMode() {
        return ThreadMode.MAIN_THREAD;
    }

    @Override
    public void onError(Call call, Exception e) {
        task.getListener().onError(e);
        DownloadManager.INSTANCE(null).stopUpdateProgress(task);
    }

    @Override
    public void onResponse(RandomAccessFile response) {

    }

    private RandomAccessFile saveFile(Response response) {
//        long start = System.currentTimeMillis();
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(this.file, "rwd");
            long seek = task.getStartPos(threadId);
            file.seek(seek);
            long downLength = 0L;
            int offset = 0;
            InputStream inStream = response.body().byteStream();
            byte[] buffer = new byte[1024 * 1024];
            while (task.getDownloadStatus() != DownloadListener.DOWNLOAD_STATUS_PAUSE
                    && task.getDownloadStatus() != DownloadListener.DOWNLOAD_STATUS_CANCEL
                    && (offset = inStream.read(buffer, 0, 1024 * 1024)) != -1) {

                file.write(buffer, 0, offset);
                downLength += offset;
                task.setSubCompletedSize(threadId, downLength);
            }
            updateById(threadId, downLength);

            long completedSize = task.addCompletedSize(downLength);
            Log.d(TAG, "completedSize:" + completedSize + ",totalSize:" + task.getTotalSize());
            if (completedSize >= task.getTotalSize()) {
                this.file.renameTo(new File(task.getSavePath(), task.getFileName()));
                task.setDownloadStatus(DownloadListener.DOWNLOAD_STATUS_COMPLETED);
            }
            file.close();
            inStream.close();
//            Log.d(TAG, "Thread%s time:%s", Thread.currentThread().getId(), System.currentTimeMillis() - start);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            DownloadManager.INSTANCE(null).stopUpdateProgress(task);
            task.getListener().onCancel();
            task.getListener().onError(e);
        } catch (IOException e) {
            e.printStackTrace();
            DownloadManager.INSTANCE(null).stopUpdateProgress(task);
            task.getListener().onError(e);
            task.getListener().onCancel();
        }

        return file;
    }


    private void updateById(int id, long completedSize) {
        DownloadRecord record = null;
        List<DownloadRecord> list = queryByUrlAndThreadId(task.getUrl(), id);
        if (!list.isEmpty()) {
            record = list.get(0);
        }

        if (null != record) {
            record.setCompletedSize(record.getCompletedSize() + completedSize);
            if (completedSize >= task.getSubContentLength(id)) {
                record.setDownloadStatus(DownloadListener.DOWNLOAD_STATUS_COMPLETED);
            }

            downloadRecordDao.update(record);
            downloadRecordDao.detach(record);
        }

    }

    private List<DownloadRecord> queryByUrlAndThreadId(String url, int id) {
        return downloadRecordDao.queryBuilder()
                .where(DownloadRecordDao.Properties.Url.eq(url), DownloadRecordDao.Properties.ThreadId.eq(id))
                .list();
    }
}

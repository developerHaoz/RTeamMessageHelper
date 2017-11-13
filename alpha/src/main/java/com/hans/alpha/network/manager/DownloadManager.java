package com.hans.alpha.network.manager;

import android.content.Context;

import com.hans.alpha.android.Log;
import com.hans.alpha.constant.MotherShipConst;
import com.hans.alpha.dao.DaoMaster;
import com.hans.alpha.dao.DownloadRecordDao;
import com.hans.alpha.network.RequestCall;
import com.hans.alpha.network.callback.DownloadCallback;
import com.hans.alpha.network.callback.SceneCallback;
import com.hans.alpha.network.factory.RequestCallFactory;
import com.hans.alpha.utils.HandlerUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

/**
 * create by changelcai on 2016/03/10
 * 多线程断点下载管理者
 */
public class DownloadManager {
    private static final String TAG = "MotherShip.DownloadManager";
    private static DownloadManager instance = null;
    private DownloadRecordDao downloadRecordDao;

    private DownloadManager(Context context) {
        DaoMaster.OpenHelper openHelper = new DaoMaster.DevOpenHelper(context, "downloadDB", null);
        DaoMaster daoMaster = new DaoMaster(openHelper.getWritableDatabase());
        downloadRecordDao = daoMaster.newSession().getDownloadRecordDao();
    }

    public static DownloadManager INSTANCE(Context context) {
        if (null == instance) {
            synchronized (DownloadManager.class) {
                if (null == instance) {
                    instance = new DownloadManager(context);
                }
            }
        }
        return instance;
    }

    List<DownloadTask> tasklist = new ArrayList<>();

    public void pause(final DownloadTask task) {
        if (null == task) {
            Log.d(TAG, "pauseByUrl task == null");
            return;
        }
        if (!tasklist.contains(task)) {
            tasklist.add(task);
        }
        task.setDownloadStatus(DownloadListener.DOWNLOAD_STATUS_PAUSE);
        List<DownloadRecord> list = queryByUrl(task.getUrl());
        for (DownloadRecord record : list) {
            record.setDownloadStatus(DownloadListener.DOWNLOAD_STATUS_PAUSE);
            downloadRecordDao.update(record);
        }
        task.getListener().onPause();
        stopUpdateProgress(task);
    }

    public void resume(final DownloadTask task) {
        if (tasklist.contains(task)) {
            tasklist.remove(task);
            downLoad(task);
        }
    }


    public void downLoad(final DownloadTask task) {
        if (null == task) {
            Log.d(TAG, "downLoad:%s", "task is null");
            return;
        } else if (DownloadListener.DOWNLOAD_STATUS_DOWNLOADING == task.getDownloadStatus()) {
            Log.d(TAG, "this task is downloading...");
            return;
        } else {
            File dir = new File(task.getSavePath());
            try {
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            task.setFile(new File(dir, task.getDownloadingName()));
            task.setDownloadStatus(DownloadListener.DOWNLOAD_STATUS_DOWNLOADING);
            RequestCallFactory.Builder(RequestCallFactory.TypeBuilder.TYPE_GET).url(task.getUrl())
                    .build().doScene(new SceneCallback<Long>() {
                @Override
                public Long parseNetworkResponse(Response response) throws IOException {
                    return response.body().contentLength();
                }

                @Override
                public int getHandleMode() {
                    return ThreadMode.NORMAL_THREAD;
                }

                @Override
                public void onError(Call call, Exception e) {
                    Log.d(TAG, "downLoad get ContentLength error %s", e.getCause());
                }

                @Override
                public void onResponse(Long totalLen) {
                    task.setTotalSize(totalLen);
                    long progress = countCompletedSize(task);
                    Log.d(TAG, "prepare download...%s", (String.format("%.1f", (progress * 1.0 / totalLen) * 100) + "%"));
                    task.getListener().onPrepare(progress, totalLen);

                    Log.d(TAG, "start download...");
                    task.getListener().onStart();
                    task.initialize(totalLen, downloadRecordDao);

                    for (int i = 0; i < MotherShipConst.DOWNLOAD_CONST.DEFAULT_THREAD_NUM; i++) {
                        if (task.getSubContentLength(i) <= 0) {
                            continue;
                        }
                        Log.d(TAG, "startPos:%s - endPos:%s", task.getStartPos(i), task.getEndPos(i));
                        RequestCall call = RequestCallFactory.Builder(RequestCallFactory.TypeBuilder.TYPE_GET).url(task.getUrl())
                                .addHeader("RANGE", "bytes=" + task.getStartPos(i) + "-" + task.getEndPos(i)).build();
                        call.doScene(new DownloadCallback(task, i, downloadRecordDao));
                        updateProgress(task);
                    }
                }
            });

        }
    }

    private static HashMap<DownloadTask, Timer> timers = new HashMap<>();

    private void updateProgress(final DownloadTask task) {
        stopUpdateProgress(task);
        Timer timer = new Timer();
        timers.put(task, timer);
        timer.schedule(new TimerTask() {
            public void run() {
                HandlerUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (task.getDownloadStatus() != DownloadListener.DOWNLOAD_STATUS_PAUSE
                                && task.getDownloadStatus() != DownloadListener.DOWNLOAD_STATUS_CANCEL
                                && task.getDownloadStatus() != DownloadListener.DOWNLOAD_STATUS_COMPLETED) {

                            task.getListener().onDownloading(task.getCompletedSize(), task.getTotalSize());
                        }

                    }
                });
                if (task.getDownloadStatus() == DownloadListener.DOWNLOAD_STATUS_COMPLETED || task.getDownloadStatus() == DownloadListener.DOWNLOAD_STATUS_ERROR) {
                    if (task.getDownloadStatus() == DownloadListener.DOWNLOAD_STATUS_COMPLETED) {
                        task.getListener().onCompleted();
                    }
                    stopUpdateProgress(task);
                    deleteAllRecordByrUrl(task);
                    cancel();
                }
            }
        }, 1000, 1500);
    }

    public void stopUpdateProgress(final DownloadTask task) {
        if (timers.containsKey(task)) {
            timers.get(task).cancel();
            timers.remove(task);
        }
    }

    private List<DownloadRecord> queryByUrl(String url) {
        return downloadRecordDao.queryBuilder()
                .where(DownloadRecordDao.Properties.Url.eq(url))
                .list();
    }

    private long countCompletedSize(DownloadTask task) {
        long pCompletedSize = 0;
        for (DownloadRecord downloadRecord : queryByUrl(task.getUrl())) {
            pCompletedSize += downloadRecord.getCompletedSize();
        }
        task.setCompletedSize(pCompletedSize);
        return pCompletedSize;
    }

    public void deleteAllRecordByrUrl(DownloadTask task) {
        downloadRecordDao.queryBuilder()
                .where(DownloadRecordDao.Properties.Url.eq(task.getUrl()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
        downloadRecordDao.detachAll();
    }
}

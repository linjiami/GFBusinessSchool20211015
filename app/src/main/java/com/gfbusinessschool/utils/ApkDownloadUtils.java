package com.gfbusinessschool.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;

import com.gfbusinessschool.R;

import java.io.File;

public class ApkDownloadUtils {

    //下载器
    private DownloadManager downloadManager;
    private Context mContext;
    //下载的ID
    private long downloadId;
    private String name;
    private ApkDownloadProgress apkDownloadProgress;

    public ApkDownloadUtils(Context context, String url, String name,ApkDownloadProgress progress ) {
        this.mContext = context;
        this.apkDownloadProgress = progress;
        downloadAPK(url, name);
        this.name = name;
    }

    //下载apk
    private void downloadAPK(String url, String name) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(mContext.getString(R.string.app_name));
        request.setDescription(mContext.getString(R.string.downloading));
        request.setVisibleInDownloadsUi(true);

        //以下两行代码可以让下载的apk文件被直接安装而不用使用Fileprovider,系统7.0或者以上才启动。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder localBuilder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(localBuilder.build());
        }
        //设置下载的路径
        File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), name);
        if (file.exists() && file.delete()){
            Utils.log("DownloadManager", "删除成功");
        }
        request.setDestinationUri(Uri.fromFile(file));
        //获取DownloadManager
        if (downloadManager == null)
            downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        if (downloadManager != null) {
            downloadId = downloadManager.enqueue(request);
        }

        //注册广播接收者，监听下载状态
        mContext.registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    //广播监听下载的各个状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    //检查下载状态
    public void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            long downloadedBytes = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            long totalBytes = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            if (apkDownloadProgress!=null) apkDownloadProgress.onResult(status,downloadedBytes,totalBytes);
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成安装APK
                    installAPK();
                    cursor.close();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    if (apkDownloadProgress!=null) apkDownloadProgress.onFailed();
                    cursor.close();
                    mContext.unregisterReceiver(receiver);
                    break;
            }
        }
    }

    public void installAPK() {
        File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), name);
        DownloadManager dManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        Intent install = new Intent(Intent.ACTION_VIEW);
        if (dManager==null) return;
        Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadId);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //参数1:上下文, 参数2:Provider主机地址 和配置文件中保持一致,参数3:共享的文件
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
        } else {
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        mContext.startActivity(install);
        if (apkDownloadProgress!=null) apkDownloadProgress.onFinish();
    }

    public interface ApkDownloadProgress{
        void onResult(int state,long downloadedBytes,long totalBytes);
        void onFinish();
        void onFailed();
    }

}
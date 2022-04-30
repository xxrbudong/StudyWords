package com.example.big.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class PlayWordsAccentUtils {
    private  String FILE_DIR_ACCENT;
    private  String FILE_ACCENT;
    public PlayWordsAccentUtils(Context context) {
        FILE_DIR_ACCENT = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath();
        FILE_ACCENT = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath() + "/";
    }

    /**
     * 下載文件
     *
     * @param name 需要保存的文件名称
     * @param urlString  存储目录 type 类型
     */
    public void downFile(String name, String urlString, String type) {
        DownloadUtil.getDownloadUtil().download(urlString,FILE_DIR_ACCENT, name + type, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                Looper.prepare();//增加部分
                //playSound(file.toString());//下载完成进行播放
                Looper.loop();//增加部分
            }

            @Override
            public void onDownloading(int progress) {
//                progressDialog.setProgress(progress);
            }

            @Override
            public void onDownloadFailed(Exception e) {

            }
        });
    }

    /**
     * 播放
     * */
    public void playSound(String path, String name) {
        Log.i("PATH:::::::::::::::::",path + name);
        MediaPlayer mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(path + name);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mMediaPlayer.start();
                }

            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (mMediaPlayer.isPlaying()) {
                    } else {
                        mMediaPlayer.release();
                    }
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

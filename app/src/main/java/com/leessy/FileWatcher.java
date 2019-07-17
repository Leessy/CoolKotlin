package com.leessy;

import android.content.Context;
import android.os.FileObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * @author ddai FileWatcher support subdirectory(recursively)
 */
public class FileWatcher extends FileObserver {
    static final String TAG = "FileWatcher";
    ArrayList<FileObserver> mObservers;
    String mPath;
    int mMask;

    String mThreadName = FileWatcher.class.getSimpleName();
    HandlerThread mThread;
    Handler mThreadHandler;

    private Context mContext;

    public FileWatcher(Context context, String path) {
        this(path, ALL_EVENTS);
        mContext = context;
    }

    public FileWatcher(String path) {
        this(path, ALL_EVENTS);
    }

    public FileWatcher(String path, int mask) {
        super(path, mask);
        mPath = path;
        mMask = mask;
    }

    @Override
    public void startWatching() {
        mThreadName = FileWatcher.class.getSimpleName();
        if (mThread == null || !mThread.isAlive()) {
            mThread = new HandlerThread(mThreadName, Process.THREAD_PRIORITY_BACKGROUND);
            mThread.start();

            mThreadHandler = new Handler(mThread.getLooper());
            mThreadHandler.post(new startRunnable());
        }
    }

    @Override
    public void stopWatching() {
        if (null != mThreadHandler && null != mThread && mThread.isAlive()) {
            mThreadHandler.post(new stopRunnable());
        }
        mThreadHandler = null;
        mThread.quit();
        mThread = null;
    }

    @Override
    public void onEvent(int event, String path) {
        event = event & FileObserver.ALL_EVENTS;
        final String tmpPath = path;
        switch (event) {
            case FileObserver.ACCESS:
                 Log.i("FileWatcher", "ACCESS: " + path);
                break;
            case FileObserver.ATTRIB:
                 Log.i("FileWatcher", "ATTRIB: " + path);
                break;
            case FileObserver.CLOSE_NOWRITE:
                 Log.i("FileWatcher", "CLOSE_NOWRITE: " + path);
                break;
            case FileObserver.CLOSE_WRITE:
                Log.i("FileWatcher", "CLOSE_WRITE: " + path);
                // 文件写入完毕后会回调，可以在这对新写入的文件做操作
                mThreadHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        //
                    }
                });
                break;
            case FileObserver.CREATE:
                Log.i(TAG, "CREATE: " + path);
                mThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        doCreate(tmpPath);
                    }
                });
                break;
            case FileObserver.DELETE:
                Log.i(TAG, "DELETE: " + path);
                mThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        doDelete(tmpPath);
                    }
                });
                break;
            case FileObserver.DELETE_SELF:
                 Log.i("FileWatcher", "DELETE_SELF: " + path);
                break;
            case FileObserver.MODIFY:
                 Log.i("FileWatcher", "MODIFY: " + path);
                break;
            case FileObserver.MOVE_SELF:
                 Log.i("FileWatcher", "MOVE_SELF: " + path);
                break;
            case FileObserver.MOVED_FROM:
                 Log.i("FileWatcher", "MOVED_FROM: " + path);
                break;
            case FileObserver.MOVED_TO:
                 Log.i("FileWatcher", "MOVED_TO: " + path);
                break;
            case FileObserver.OPEN:
                Log.i("FileWatcher", "OPEN: " + path);
                break;
            default:
                Log.i(TAG, "DEFAULT(" + event + ";) : " + path);
                break;
        }
    }

    private void doCreate(String path) {
        synchronized (FileWatcher.this) {
            File file = new File(path);
            if (!file.exists()) {
                return;
            }

            if (file.isDirectory()) {
                // 新建文件夹，对该文件夹及子目录添加监听
                Stack<String> stack = new Stack<String>();
                stack.push(path);

                while (!stack.isEmpty()) {
                    String parent = stack.pop();
                    SingleFileObserver observer = new SingleFileObserver(parent, mMask);
                    observer.startWatching();

                    mObservers.add(observer);
                    Log.d(TAG, "add observer " + parent);
                    File parentFile = new File(parent);
                    File[] files = parentFile.listFiles();
                    if (null == files) {
                        continue;
                    }

                    for (File f : files) {
                        if (f.isDirectory() && !f.getName().equals(".") && !f.getName().equals("..")) {
                            stack.push(f.getPath());
                        }
                    }
                }

                stack.clear();
                stack = null;
            } else {
                // 新建文件
            }
        }
    }

    private void doDelete(String path) {
        synchronized (FileWatcher.this) {
            Iterator<FileObserver> it = mObservers.iterator();
            while (it.hasNext()) {
                SingleFileObserver sfo = (SingleFileObserver) it.next();
                // 如果删除的是文件夹移除对该文件夹及子目录的监听
                if (sfo.mPath != null && (sfo.mPath.equals(path) || sfo.mPath.startsWith(path + "/"))) {
                    Log.d(TAG, "stop observer " + sfo.mPath);
                    sfo.stopWatching();
                    it.remove();
                    sfo = null;
                }
            }
        }
    }

    /**
     * Monitor single directory and dispatch all events to its parent, with full
     * path.
     */
    class SingleFileObserver extends FileObserver {
        String mPath;

        public SingleFileObserver(String path) {
            this(path, ALL_EVENTS);
            mPath = path;
        }

        public SingleFileObserver(String path, int mask) {
            super(path, mask);
            mPath = path;
        }

        @Override
        public void onEvent(int event, String path) {
            if (path == null) {
                return;
            }
            String newPath = mPath + "/" + path;
            FileWatcher.this.onEvent(event, newPath);
        }
    }

    class startRunnable implements Runnable {
        @Override
        public void run() {
            synchronized (FileWatcher.this) {
                if (mObservers != null) {
                    return;
                }

                mObservers = new ArrayList<FileObserver>();
                Stack<String> stack = new Stack<String>();
                stack.push(mPath);

                while (!stack.isEmpty()) {
                    String parent = String.valueOf(stack.pop());
                    mObservers.add(new SingleFileObserver(parent, mMask));
                    File path = new File(parent);
                    File[] files = path.listFiles();
                    if (null == files) {
                        continue;
                    }

                    for (File f : files) {
                        if (f.isDirectory() && !f.getName().equals(".") && !f.getName().equals("..")) {
                            stack.push(f.getPath());
                        }
                    }
                }

                Iterator<FileObserver> it = mObservers.iterator();
                while (it.hasNext()) {
                    SingleFileObserver sfo = (SingleFileObserver) it.next();
                    sfo.startWatching();
                }
            }
        }
    }

    class stopRunnable implements Runnable {
        @Override
        public void run() {
            synchronized (FileWatcher.this) {
                if (mObservers == null) {
                    return;
                }

                Iterator<FileObserver> it = mObservers.iterator();
                while (it.hasNext()) {
                    FileObserver sfo = it.next();
                    sfo.stopWatching();
                }
                mObservers.clear();
                mObservers = null;
            }
        }
    }
}
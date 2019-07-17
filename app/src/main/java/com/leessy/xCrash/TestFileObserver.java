package com.leessy.xCrash;

import android.os.FileObserver;
import android.util.Log;


///** Event type: Data was read from a file */
//public static final int ACCESS = 0x00000001;
///** Event type: Data was written to a file */
//public static final int MODIFY = 0x00000002;
///** Event type: Metadata (permissions, owner, timestamp) was changed explicitly */
//public static final int ATTRIB = 0x00000004;
///** Event type: Someone had a file or directory open for writing, and closed it */
//public static final int CLOSE_WRITE = 0x00000008;
///** Event type: Someone had a file or directory open read-only, and closed it */
//public static final int CLOSE_NOWRITE = 0x00000010;
///** Event type: A file or directory was opened */
//public static final int OPEN = 0x00000020;
///** Event type: A file or subdirectory was moved from the monitored directory */
//public static final int MOVED_FROM = 0x00000040;
///** Event type: A file or subdirectory was moved to the monitored directory */
//public static final int MOVED_TO = 0x00000080;
///** Event type: A new file or subdirectory was created under the monitored directory */
//public static final int CREATE = 0x00000100;
///** Event type: A file was deleted from the monitored directory */
//public static final int DELETE = 0x00000200;
///** Event type: The monitored file or directory was deleted; monitoring effectively stops */
//public static final int DELETE_SELF = 0x00000400;
///** Event type: The monitored file or directory was moved; monitoring continues */
//public static final int MOVE_SELF = 0x00000800;
// TestFileObserver 文件观察类
public class TestFileObserver extends FileObserver {

    // path 为 需要监听的文件或文件夹
    public TestFileObserver(String path) {
        super(path, FileObserver.ALL_EVENTS);
    }

    @Override
    public void onEvent(int event, String path) {
        Log.e("wannoo", "文件操作 ***" + path );

        int e = event & FileObserver.ALL_EVENTS;
        switch (e) {
            case FileObserver.ACCESS:
                Log.e("wannoo", "文件操作___" + e + "__1打开文件后读取文件的操作");
                break;
            case FileObserver.MODIFY:
                Log.e("wannoo", "文件操作___" + e + "__2文件被修改");
                break;
            case FileObserver.ATTRIB:
                Log.e("wannoo", "文件操作___" + e + "__4属性变化");
                break;
            case FileObserver.CLOSE_WRITE:
                Log.e("wannoo", "文件操作___" + e + "__8文件写入或编辑后关闭");
                break;
            case FileObserver.CLOSE_NOWRITE:
                Log.e("wannoo", "文件操作___" + e + "__16只读文件被关闭");
                break;
            case FileObserver.OPEN:
                Log.e("wannoo", "文件操作___" + e + "__32文件被打开");
                break;
            case FileObserver.MOVED_FROM:
                Log.e("wannoo", "文件操作___" + e + "__64移出事件");//试了重命名先MOVED_FROM再MOVED_TO
                break;
            case FileObserver.MOVED_TO:
                Log.e("wannoo", "文件操作___" + e + "__128移入事件");
                break;
            case FileObserver.CREATE:
                Log.e("wannoo", "文件操作___" + e + "__256新建文件");//把文件移动给自己先CREATE在DELETE
                break;
            case FileObserver.DELETE:
                Log.e("wannoo", "文件操作___" + e + "__512有删除文件");//把文件移出去DELETE
                break;
            case FileObserver.DELETE_SELF:
                Log.e("wannoo", "文件操作___" + e + "__1024监听的这个文件夹被删除");
                break;
            case FileObserver.MOVE_SELF:
                Log.e("wannoo", "文件操作___" + e + "__2048监听的这个文件夹被移走");
                break;
            case FileObserver.ALL_EVENTS:
                Log.e("wannoo", "文件操作___" + e + "__4095全部操作");
                break;
            default:
        }
    }
}

package com.Util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

/**
 * 常用 设备操作方法
 *
 * @author Created by 刘承. on 2018/8/9
 * business@onfacemind.com
 */
public class CommonTool {
    private static final String TAG = "CommonTool";

    /**
     * 关闭Android导航栏，实现全屏
     */
    public static void closeBar() {
        try {
            String command;
            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib service call activity 42 s16 com.android.systemui";
            ArrayList<String> envlist = new ArrayList<String>();
            Map<String, String> env = System.getenv();
            for (String envName : env.keySet()) {
                envlist.add(envName + "=" + env.get(envName));
            }
            String[] envp = envlist.toArray(new String[0]);
            Process proc = Runtime.getRuntime().exec(
                    new String[]{"su", "-c", command}, envp);
            proc.waitFor();

        } catch (Exception ex) {
        }
    }

    /**
     * 显示导航栏
     */
    public static void showBar() {
        try {
            String command;
            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib am startservice -n com.android.systemui/.SystemUIService";
            ArrayList<String> envlist = new ArrayList<String>();
            Map<String, String> env = System.getenv();
            for (String envName : env.keySet()) {
                envlist.add(envName + "=" + env.get(envName));
            }
            String[] envp = envlist.toArray(new String[0]);
            Process proc = Runtime.getRuntime().exec(
                    new String[]{"su", "-c", command}, envp);
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //拷贝文件
    public static boolean cmd_copy(String dr1, String dr2) {
        android.util.Log.d(TAG, "cmd_copy: start" + dr1 + "--" + dr2);
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command1 = "chmod 777 " + dr1 + "\n";
            String command = "cp " + dr1 + " " + dr2 + "\n";//有效 -2文件无效
//            String command = "dd if=" + dr1 + " of=" + dr2;//有效
            String command2 = "chmod 777 " + dr2 + "\n";
            dataOutputStream.write(command1.getBytes(Charset.forName("utf-8")));
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.write(command2.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            android.util.Log.d(TAG, "cmd_copy: end");
            result = true;
        } catch (Exception e) {
            android.util.Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                android.util.Log.e(TAG, e.getMessage(), e);
            }
        }
        return result;
    }
}

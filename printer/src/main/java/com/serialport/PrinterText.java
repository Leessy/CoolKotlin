package com.serialport;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;

/**
 * 作者：陈博
 * 创建时间：2018/8/25 0025 14:56
 */
public class PrinterText {

    private static PrinterText printerText;
    private SerialPortNew mSerialPort = null;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    SerialPortManager serialPortManager;

    public static PrinterText instance() {
        if (printerText == null) {
            synchronized (PrinterText.class) {
                printerText = new PrinterText();
            }
        }
        return printerText;
    }

    /**
     * 创建
     */
    public boolean create(String s) {
        if (mSerialPort == null) {
            try {
                mSerialPort = getSerialPort(s);
                if (mSerialPort==null){
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mOutputStream!=null&&mInputStream!=null){
            //sendCommand();
            //mInputStream.read();
            return true;
        }else {
            return false;
        }
    }

    private SerialPortNew getSerialPort(String s) throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            String path = s;
            int baudrate = 115200;
            try {
                serialPortManager=new SerialPortManager();
                serialPortManager.openSerialPort(new File(path), baudrate);
                mSerialPort = new SerialPortNew();
                mOutputStream = serialPortManager.mFileOutputStream;
                mInputStream = serialPortManager.mFileInputStream;
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mSerialPort;
    }

    /**
     * 发送打印命令
     *
     * @param command
     */
    public void sendCommand(int... command) {
        try {
            for (int i = 0; i < command.length; i++) {
                mOutputStream.write(command[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印文字内容
     * @param tString 文字内容
     */
    public void sendString(String tString) {
        String string = null;
        string = UnicodeToGBK(tString);
        try {
            for (int i = 0; i < string.length(); i++) {

                mOutputStream.write((int) string.charAt(i));
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /*GBK TO  UNICODE???*/
    private static String UnicodeToGBK(String s) {
        try {
            String newstring = null;
            newstring = new String(s.getBytes("GBK"), "ISO-8859-1");
            return newstring;
        } catch (UnsupportedEncodingException e) {

            return s;
        }
    }

    /**
     * 打印图片位图数据
     * @param bmp
     * @return
     * @throws IOException
     */
    public void printmap(Bitmap bmp) throws IOException {

        if (bmp == null) {
            return ;
        }
        int[] pixels = new int[bmp.getByteCount()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        int aHeight = bmp.getHeight();
        int aWeight = bmp.getWidth();

        int modh = 0;
        int modw = 0;
        int height = aHeight;
        int weight = aWeight;
        if (height % 24 != 0) {
            modh = height % 24;
            height += 24 - modh;
        }
        if (weight % 8 != 0) {
            modw = weight % 8;
            weight = weight + 8 - modw;
        }
        int width = weight * 3;
        byte[] body = new byte[width];
        sendCommand(0x1B, 0x41, 0x01);    //图片居中指令
        for (int i = 0; i < height; i += 24) {
            PrintImaageHead(weight);
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < 24; k++) {
                    int color = 0;
                    int r = 0;
                    int g = 0;
                    int b = 0;
                    if ((i + k >= aHeight) || (j / 3 >= aWeight)) {
                        b = g = r = 111;
                    } else {
                        color = pixels[((i + k) * aWeight + j / 3)];
                        r = 0xFF & color;
                        g = (0xFF00 & color) >> 8;
                        b = (0xFF0000 & color) >> 16;
                    }
                    if ((k == 8) || (k == 16)) {
                        j++;
                    }
                    if ((r < 100) && (g < 100) && (b < 100)) {
                        body[j] = ((byte) (body[j] * 2 + 1));
                    } else {
                        body[j] = ((byte) (body[j] * 2));
                    }
                }
            }
            PrintByte(body);
        }
    }

    /**
     * 先下发头信息
     * @param weight
     */
    private void PrintImaageHead(int weight) {
        byte[] head = {27, 42, 33, 0, 0};
        if (weight > 255) {
            head[3] = ((byte) (weight - 256));
            head[4] = 1;
        } else {
            head[3] = ((byte) weight);
            head[4] = 0;
        }
        PrintByte(head);
    }

    /**
     *
     * @param callerame 访客姓名
     * @param targetName 被 访 人
     * @param purpose 事    由
     * @param time 来访时间
     */
    public void printer(String callerame, String targetName, String purpose, String time) {
        if (this.mSerialPort != null && this.mOutputStream != null) {
            this.sendCommand(27, 97, 1);
            this.sendCommand(27, 85, 3);
            this.sendCommand(27, 86, 2);
            this.sendString("访客单");
            this.sendCommand(10, 10);
            this.sendCommand(27, 97, 0);
            this.sendCommand(27, 33, 16);
            this.sendString("访客姓名: " + callerame);
            this.sendCommand(10);
            this.sendString("被 访 人: " + targetName);
            this.sendCommand(10);
            this.sendString("事    由: " + purpose);
            this.sendCommand(10);
            this.sendString("来访时间: " + time);
            this.sendCommand(10, 10);

            this.sendCommand(10);
            this.sendString("被访人签名： ");
            this.sendCommand(10, 10, 10);
        }

    }

    /**
     * 打印二维码byte
     * @param data
     * @return
     */
    private int PrintByte(byte[] data) {
        try {
            mOutputStream.write(data);
            //sleep(100);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 关闭串口
     */
    public void closePrinter() {
        if (serialPortManager!=null){
            serialPortManager.closeSerialPort();
            mSerialPort=null;
        }

        try {
            if (mOutputStream != null) {
                mOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (mInputStream != null) {
                mInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

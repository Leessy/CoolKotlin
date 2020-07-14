package com.printer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;


/**
 * 将图片转化为二进制
 *
 * @author 刘承
 */
public class PicFromPrintUtils {
    private static final String TAG = "PicFromPrintUtils";

    /**
     * 我们的热敏打印机是RP-POS80S或RP-POS80P或RP-POS80CS或RP-POS80CP打印机
     * 360*360的图片，8个字节（8个像素点）是一个二进制，将二进制转化为十进制数值
     * y轴：24个像素点为一组，即360就是15组（0-14）
     * x轴：360个像素点（0-359）
     * 里面的每一组（24*360），每8个像素点为一个二进制，（每组有3个，3*8=24）
     * /
     * /**
     * 把一张Bitmap图片转化为打印机可以打印的bit(将图片压缩为360*360)
     * 效率很高（相对于下面）
     *
     * @param bmp
     * @return
     */
    public static byte[] draw2PxPoint(Bitmap bmp) {
        //用来存储转换后的 bitmap 数据。为什么要再加1000，这是为了应对当图片高度无法
        //整除24时的情况。比如bitmap 分辨率为 240 * 250，占用 7500 byte，5:5455,3,5447,4,5427
        //但是实际上要存储11行数据，每一行需要 24 * 240 / 8 =720byte 的空间。再加上一些指令存储的开销，
        //所以多申请 1000byte 的空间是稳妥的，不然运行时会抛出数组访问越界的异常。
        int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
        byte[] data = new byte[size];
        int k = 0;
        //设置行距为0的指令
        data[k++] = 0x1B;
        data[k++] = 0x33;
        data[k++] = 0x00;
        // 逐行打印
        for (int j = 0; j < bmp.getHeight() / 24f; j++) {// 每行24像素，最好高为24倍数,
            //打印图片的指令
            data[k++] = 0x1B;
            data[k++] = 0x2A;
            data[k++] = 33;
            data[k++] = (byte) (bmp.getWidth() % 256); //nL
            data[k++] = (byte) (bmp.getWidth() / 256); //nH
            //对于每一行，逐列打印
            for (int i = 0; i < bmp.getWidth(); i++) { //每一列24个像素点，分为3个字节存储  最好也为24的倍数
                for (int m = 0; m < 3; m++) {
                    //每个字节表示8个像素点，0表示白色，1表示黑色
                    for (int n = 0; n < 8; n++) {
                        if ((j * 24 + m * 8 + n) >= bmp.getHeight()) {//高度不为24整倍数时 超高异常
                            break;
                        }
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
                        data[k] += data[k] + b;
                    }
                    k++;
                }
            }
//            data[k++] = 10;//换行
        }
        //   long a=System.currentTimeMillis();
        byte[] data1 = new byte[k];
        System.arraycopy(data, 0, data1, 0, k);
        // long b=System.currentTimeMillis();
        //  System.out.println("结束字节:"+k+"---"+data.length+"耗时:"+(b-a));
        return data1;
    }


    /**
     * 把一张Bitmap图片转化为打印机可以打印的bit
     * byte数组=分辨率/24*3*分辨率+5*分辨率/24*3
     * 分辨率大于256时
     * n1=分辨率/256取余（n1%256）
     * n2=除得的整数
     * 分辨率小于256时
     * n1=自身
     * n2=0
     * i=分辨率/24
     * x=分辨率
     *
     * @param bit
     * @return
     */
    public static byte[] pic2PxPoint(Bitmap bit) {
        byte[] data = new byte[1230];
        int k = 0;
        for (int i = 0; i < 4; i++) {
            data[k++] = 0x1B;
            data[k++] = 0x2A;
            data[k++] = 33; // m=33时，选择24点双密度打印，分辨率达到200DPI。
            data[k++] = 0x60;//n1
            data[k++] = 0x00;//n2
            for (int x = 0; x < 96; x++) {
                for (int m = 0; m < 3; m++) {
                    byte[] by = new byte[8];
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(x, i * 24 + m * 8 + 7 - n, bit);
                        by[n] = b;
                    }
                    data[k] = (byte) changePointPx1(by);
                    k++;
                }
            }
            data[k++] = 10;
        }
        return data;
    }


    /**
     * 图片二值化，黑色是1，白色是0
     *
     * @param x   横坐标
     * @param y   纵坐标
     * @param bit 位图
     * @return
     */
    public static byte px2Byte(int x, int y, Bitmap bit) {
        byte b;
        int pixel = bit.getPixel(x, y);
        int red = (pixel & 0x00ff0000) >> 16; // 取高两位
        int green = (pixel & 0x0000ff00) >> 8; // 取中两位
        int blue = pixel & 0x000000ff; // 取低两位
        int gray = RGB2Gray(red, green, blue);
        if (gray < 128) {
            b = 1;
        } else {
            b = 0;
        }
        return b;
    }


    /**
     * 图片灰度的转化
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    private static int RGB2Gray(int r, int g, int b) {
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b);  //灰度转化公式
        return gray;
    }


    /**
     * 对图片进行压缩（去除透明度）
     *
     * @param bitmapOrg
     */
    public static Bitmap compressPic(Bitmap bitmapOrg) {
// 获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
// 定义预转换成的图片的宽度和高度
        int newWidth = 96;
        int newHeight = 96;
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }

    /**
     * 对图片进行压缩(不去除透明度)
     *
     * @param bitmapOrg
     */
    public static Bitmap compressBitmap(Bitmap bitmapOrg, int newWidth, int newHeight) {
// 加载需要操作的图片，这里是一张图片
//               Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),R.drawable.alipay);
// 获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
// 定义预转换成的图片的宽度和高度
//        int newWidth = 360;
//        int newHeight = 360;
// 计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
// 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
// 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
// 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);
// 将上面创建的Bitmap转换成Drawable对象，使得其可以使用在ImageView, ImageButton中
//               BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
        return resizedBitmap;
    }


    /**
     * 将[1,0,0,1,0,0,0,1]这样的二进制转为化十进制的数值（效率更高）
     *
     * @param arry
     * @return
     */
    public static int changePointPx1(byte[] arry) {
        int v = 0;
        for (int j = 0; j < arry.length; j++) {
            if (arry[j] == 1) {
                v = v | 1 << j;
            }
        }
        return v;
    }


    /**
     * 将[1,0,0,1,0,0,0,1]这样的二进制转为化十进制的数值
     *
     * @param arry
     * @return
     */
    public byte changePointPx(byte[] arry) {
        byte v = 0;
        for (int i = 0; i < 8; i++) {
            v += v + arry[i];
        }
        return v;
    }


    /**
     * 得到位图的某个点的像素值
     *
     * @param bitmap
     * @return
     */
    public byte[] getPicPx(Bitmap bitmap) {
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];// 保存所有的像素的数组，图片宽×高
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int i = 0; i < pixels.length; i++) {
            int clr = pixels[i];
            int red = (clr & 0x00ff0000) >> 16; // 取高两位
            int green = (clr & 0x0000ff00) >> 8; // 取中两位
            int blue = clr & 0x000000ff; // 取低两位
            System.out.println("r=" + red + ",g=" + green + ",b=" + blue);
        }
        return null;
    }


}
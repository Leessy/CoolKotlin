package com.leessy;

import android.graphics.*;

import java.io.ByteArrayOutputStream;

/**
 * YUV420SP 转换的RGB24   再转换bitmap
 */
public class MyBitmapFactory {
    /**
     * YUV420sp原始预览数据转 bitmap
     *
     * @param bytes
     * @param w
     * @param h
     * @return
     */
    /////转换用时 80-130
    public static Bitmap YUV420SPDataToBitmap(byte[] bytes, int w, int h) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        YuvImage yuvimage = new YuvImage(bytes, ImageFormat.NV21, w, h, null);
        ByteArrayOutputStream baos;
        byte[] rawImage;
        baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, w, h), 100, baos);// 80--JPG图片的质量[0-100],100最高
        rawImage = baos.toByteArray();
//            将rawImage转换成bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
        return bitmap;
    }

    /*
     * byte[] data保存的是纯RGB的数据，而非完整的图片文件数据
     */
    public static Bitmap createMyBitmap(byte[] data, int width, int height) {
        int[] colors = convertByteToColor(data);
        if (colors == null) {
            return null;
        }

        Bitmap bmp = null;

        try {
            bmp = Bitmap.createBitmap(colors, 0, width, width, height,
                    Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
            // TODO: handle exception

            return null;
        }

        return bmp;
    }


    /*
     * 将RGB数组转化为像素数组
     */
    private static int[] convertByteToColor(byte[] data) {
        int size = data.length;
        if (size == 0) {
            return null;
        }
        // 理论上data的长度应该是3的倍数，这里做个兼容
        int arg = 0;
        if (size % 3 != 0) {
            arg = 1;
        }
        int[] color = new int[size / 3 + arg];
        if (arg == 0) {                                  //  正好是3的倍数
            for (int i = 0; i < color.length; ++i) {

                //bgr顺序排列
                color[i] = (data[i * 3 + 2] << 16 & 0x00FF0000) |
                        (data[i * 3 + 1] << 8 & 0x0000FF00) |
                        (data[i * 3] & 0x000000FF) |
                        0xFF000000;
                //rgb顺序排列
//                color[i] = (data[i * 3] << 16 & 0x00FF0000) |
//                        (data[i * 3 + 1] << 8 & 0x0000FF00) |
//                        (data[i * 3 + 2] & 0x000000FF) |
//                        0xFF000000;
            }
        } else {                                      // 不是3的倍数
            for (int i = 0; i < color.length - 1; ++i) {
                color[i] = (data[i * 3 + 2] << 16 & 0x00FF0000) |
                        (data[i * 3 + 1] << 8 & 0x0000FF00) |
                        (data[i * 3] & 0x000000FF) |
                        0xFF000000;
//                color[i] = (data[i * 3] << 16 & 0x00FF0000) |
//                        (data[i * 3 + 1] << 8 & 0x0000FF00) |
//                        (data[i * 3 + 2] & 0x000000FF) |
//                        0xFF000000;
            }
            color[color.length - 1] = 0xFF000000;                   // 最后一个像素用黑色填充
        }
        return color;
    }

}   
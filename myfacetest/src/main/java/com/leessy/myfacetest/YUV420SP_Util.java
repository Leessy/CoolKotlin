package com.leessy.myfacetest;

import android.graphics.*;

import java.io.ByteArrayOutputStream;

/**
 * @author Created by 刘承. on 2018/10/11
 * business@onfacemind.com
 */
public class YUV420SP_Util {
    /**
     * YUV420sp原始预览数据转 bitmap
     *
     * @param bytes
     * @param w
     * @param h
     * @return
     */
    public static Bitmap JUV420SPDataToBitmap(byte[] bytes, int w, int h) {
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
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
        return bitmap;

    }

    /**
     * YUV420SP转RGB24
     *
     * @param yuv420sp
     * @param width
     * @param height
     * @return
     */
    public static byte[] yuv420sp2Rgb24(byte[] yuv420sp, int width, int height) {
        final int frameSize = width * height;
        byte[] rgb = new byte[width * height * 3];
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0) {
                    y = 0;
                }
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0) {
                    r = 0;
                } else if (r > 262143) {
                    r = 262143;
                }
                if (g < 0) {
                    g = 0;
                } else if (g > 262143) {
                    g = 262143;
                }
                if (b < 0) {
                    b = 0;
                } else if (b > 262143) {
                    b = 262143;
                }

                rgb[yp * 3 + 0] = (byte) ((r >> 10) & 0xff);
                rgb[yp * 3 + 1] = (byte) ((g >> 10) & 0xff);
                rgb[yp * 3 + 2] = (byte) ((b >> 10) & 0xff);
            }
        }
        return rgb;
    }

    //

    /**
     * 身份证 bitmap转420sp
     *
     * @param mBitmap
     * @return
     */
    //    byte[] BitmapToYUV420SP(byte[] yuv420sp) {
    public static byte[] BitmapToYUV420SP(Bitmap mBitmap) {
        int mWidth = mBitmap.getWidth();
        int mHeight = mBitmap.getHeight();

        int[] mIntArray = new int[mWidth * mHeight];

        // Copy pixel data from the Bitmap into the 'intArray' array
        mBitmap.getPixels(mIntArray, 0, mWidth, 0, 0, mWidth, mHeight);

        // Call to encoding function : convert intArray to Yuv Binary data
        byte[] b = new byte[mWidth * mHeight * 3];
        return encodeYUV420SP(b, mIntArray, mWidth, mHeight);

    }

    /**
     * 将bitmap里得到的argb数据转成yuv420sp格式
     * 这个yuv420sp数据就可以直接传给MediaCodec,通过AvcEncoder间接进行编码
     *
     * @param yuv420sp 用来存放yuv429sp数据
     * @param argb     传入argb数据
     * @param width    图片width
     * @param height   图片height
     */
    private static byte[] encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
        final int frameSize = width * height;

        int yIndex = 0;
        int uvIndex = frameSize;

        int a, R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

                a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
                R = (argb[index] & 0xff0000) >> 16;
                G = (argb[index] & 0xff00) >> 8;
                B = (argb[index] & 0xff) >> 0;

                // well known RGB to YUV algorithm
                Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
                U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
                V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;

                // NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
                //    meaning for every 4 Y pixels there are 1 V and 1 U.  Note the sampling is every other
                //    pixel AND every other scanline.
                yuv420sp[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv420sp[uvIndex++] = (byte) ((V < 0) ? 0 : ((V > 255) ? 255 : V));
                    yuv420sp[uvIndex++] = (byte) ((U < 0) ? 0 : ((U > 255) ? 255 : U));
                }

                index++;
            }
        }
        return yuv420sp;
    }
    //    static public byte[] encodeYUV420SP(byte[] yuv420sp, int[] rgba, int width, int height) {
//        final int frameSize = width * height;
//
//        int[] U, V;
//        U = new int[frameSize];
//        V = new int[frameSize];
//
//        final int uvwidth = width / 2;
//
//        int r, g, b, y, u, v;
//        for (int j = 0; j < height; j++) {
//            int index = width * j;
//            for (int i = 0; i < width; i++) {
//
//                r = Color.red(rgba[index]);
//                g = Color.green(rgba[index]);
//                b = Color.blue(rgba[index]);
//
//                // rgb to yuv
//                y = (66 * r + 129 * g + 25 * b + 128) >> 8 + 16;
//                u = (-38 * r - 74 * g + 112 * b + 128) >> 8 + 128;
//                v = (112 * r - 94 * g - 18 * b + 128) >> 8 + 128;
//
//                // clip y
//                yuv420sp[index] = (byte) ((y < 0) ? 0 : ((y > 255) ? 255 : y));
//                U[index] = u;
//                V[index++] = v;
//            }
//        }
//        return yuv420sp;
//    }


    /**
     * YUV420sp原始数据旋转 90度
     *
     * @param src
     * @param srcWidth
     * @param srcHeight
     * @return
     */
    public static byte[] YUV420spRotate90Clockwise(byte[] src, int srcWidth, int srcHeight) {
        byte[] dst = new byte[src.length];

        int wh = srcWidth * srcHeight;
        int uvHeight = srcHeight >> 1;

        //旋转Y
        int k = 0;
        for (int i = 0; i < srcWidth; i++) {
            int nPos = 0;
            for (int j = 0; j < srcHeight; j++) {
                dst[k] = src[nPos + i];
                k++;
                nPos += srcWidth;
            }
        }

        for (int i = 0; i < srcWidth; i += 2) {
            int nPos = wh;
            for (int j = 0; j < uvHeight; j++) {
                dst[k] = src[nPos + i];
                dst[k + 1] = src[nPos + i + 1];
                k += 2;
                nPos += srcWidth;
            }
        }
        return dst;
    }


    /**
     * yuv逆时针旋转90
     *
     * @param src
     * @param width
     * @param height
     * @return
     */
    public static byte[] YUV420spRotate90Anticlockwise(byte[] src, int width, int height) {
        byte[] dst = new byte[src.length];

        int wh = width * height;
        int uvHeight = height >> 1;

        //旋转Y
        int k = 0;
        for (int i = 0; i < width; i++) {
            int nPos = width - 1;
            for (int j = 0; j < height; j++) {
                dst[k] = src[nPos - i];
                k++;
                nPos += width;
            }
        }

        for (int i = 0; i < width; i += 2) {
            int nPos = wh + width - 1;
            for (int j = 0; j < uvHeight; j++) {
                dst[k] = src[nPos - i - 1];
                dst[k + 1] = src[nPos - i];
                k += 2;
                nPos += width;
            }
        }

        return dst;
    }

    /**
     * 旋转270度
     *
     * @param src
     * @param width
     * @param height
     * @return
     */
    public static byte[] YUV420spRotate270(byte[] src, int width, int height) {
        byte[] des = new byte[src.length];

        int n = 0;
        int uvHeight = height >> 1;
        int wh = width * height;
        //copy y
        for (int j = width - 1; j >= 0; j--) {
            for (int i = 0; i < height; i++) {
                des[n++] = src[width * i + j];
            }
        }

        for (int j = width - 1; j > 0; j -= 2) {
            for (int i = 0; i < uvHeight; i++) {
                des[n++] = src[wh + width * i + j - 1];
                des[n++] = src[wh + width * i + j];
            }
        }
        return des;
    }

    // untested function

    /**
     * 将bitmap转回 YUV420SP （***）
     *
     * @param inputWidth
     * @param inputHeight
     * @param scaled
     * @return
     */
    public static byte[] getNV21(int inputWidth, int inputHeight, Bitmap scaled) {

        int[] argb = new int[inputWidth * inputHeight];

        scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);

        byte[] yuv = new byte[inputWidth * inputHeight * 3 / 2];
        getNV21encodeYUV420SP(yuv, argb, inputWidth, inputHeight);

        scaled.recycle();

        return yuv;
    }

    private static void getNV21encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
        final int frameSize = width * height;

        int yIndex = 0;
        int uvIndex = frameSize;

        int a, R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

                a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
                R = (argb[index] & 0xff0000) >> 16;
                G = (argb[index] & 0xff00) >> 8;
                B = (argb[index] & 0xff) >> 0;

                // well known RGB to YUV algorithm
                Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
                U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
                V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;

                // NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
                //    meaning for every 4 Y pixels there are 1 V and 1 U.  Note the sampling is every other
                //    pixel AND every other scanline.
                yuv420sp[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv420sp[uvIndex++] = (byte) ((V < 0) ? 0 : ((V > 255) ? 255 : V));
                    yuv420sp[uvIndex++] = (byte) ((U < 0) ? 0 : ((U > 255) ? 255 : U));
                }

                index++;
            }
        }
    }

    //Y Y Y Y ... U V . --NV12       Y Y Y Y ... V U--NV21
    public static void NV12ToYuv420P(byte[] nv12, byte[] yuv420p, int width, int height) {
        int ySize = width * height;
        int i, j;
//y
        for (i = 0; i < ySize; i++) {
            yuv420p[i] = nv12[i];
        }
//u
        i = 0;
        for (j = 0; j < ySize / 2; j += 2) {
            yuv420p[ySize + i] = nv12[ySize + j];
            i++;
        }
//v
        i = 0;
        for (j = 1; j < ySize / 2; j += 2) {
            yuv420p[ySize * 5 / 4 + i] = nv12[ySize + j];
            i++;
        }

    }

    //Y Y Y Y ... U V . --NV12       Y Y Y Y ... V U--NV21
    public static void NV21ToYuv420P(byte[] nv12, byte[] yuv420p, int width, int height) {
        int ySize = width * height;
        int i, j;
        //y
        for (i = 0; i < ySize; i++) {
            yuv420p[i] = nv12[i];
        }
        //V
        i = 0;
        for (j = 0; j < ySize / 2; j += 2) {
            yuv420p[ySize * 5 / 4 + i] = nv12[ySize + j];
            i++;
        }
        //U
        i = 0;
        for (j = 1; j < ySize / 2; j += 2) {
            yuv420p[ySize + i] = nv12[ySize + j];
            i++;
        }
    }

    /**
     * RGB24旋转90度
     */
//    RGB24旋转90度
//    void RGBRotate90(BYTE *des,BYTE *src,int width,int height)
//    {
//
//        if ((!des)||(!src))
//        {
//            return;
//        }
//
//        int n = 0;
//        int linesize = width*3;
//        int i,j;
//        for (j=width;j>0;j--)
//            for (i=0;i<height;i++)
//            {
//                memcpy(&des[n],&src[linesize*i+j*3-3],3);
//                n+=3;
//            }
//    /*
//    if((!des)||(!src))
//    {
//        return;
//    }
//    int n = 0;
//    int linesize = width*3;
//    int i;
//    int j;
//    // 顺时针的旋转的算法
//    for(j = 0;j < width ;j++)
//        for(i= height;i>0;i--)
//        {
//            memcpy(&des[n],&src[linesize*(i-1)+j*3-3],3);
//            n+=3;
//        }
//    */
//    }
}

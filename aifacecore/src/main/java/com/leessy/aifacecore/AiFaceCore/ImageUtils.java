//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.leessy.aifacecore.AiFaceCore;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;

import java.nio.ByteBuffer;

public class ImageUtils {
    private static final int FACE_DETECT_IMAGE_WIDTH_LIMIT = 4;
    private static final int FACE_DETECT_IMAGE_HEIGHT_LIMIT = 2;
    private static final int VALUE_FOR_4_ALIGN = 3;
    private static final int VALUE_FOR_2_ALIGN = 1;

    public ImageUtils() {
    }

    public static byte[] bitmapToBgr24(Bitmap image) {
        if (image == null) {
            return null;
        } else {
            if (image.getConfig() != Config.ARGB_8888) {
                image = image.copy(Config.ARGB_8888, true);
            }

            int bytes = image.getByteCount();
            ByteBuffer buffer = ByteBuffer.allocate(bytes);
            image.copyPixelsToBuffer(buffer);
            byte[] rgbaData = buffer.array();
            int pixelCount = rgbaData.length / 4;
            byte[] bgrData = new byte[pixelCount * 3];
            int rgbaIndex = 0;
            int bgrIndex = 0;

            for (int i = 0; i < pixelCount; ++i) {
                bgrData[bgrIndex] = rgbaData[rgbaIndex + 2];
                bgrData[bgrIndex + 1] = rgbaData[rgbaIndex + 1];
                bgrData[bgrIndex + 2] = rgbaData[rgbaIndex];
                bgrIndex += 3;
                rgbaIndex += 4;
            }

            return bgrData;
        }
    }

    public static Bitmap bgrToBitmap(byte[] bgr, int width, int height) {
        int[] colors = convertBgrToColor(bgr);
        return colors == null ? null : Bitmap.createBitmap(colors, 0, width, width, height, Config.ARGB_8888);
    }

    private static int[] convertBgrToColor(byte[] bgr) {
        int size = bgr.length;
        if (size == 0) {
            return null;
        } else if (size % 3 != 0) {
            return null;
        } else {
            int[] color = new int[size / 3];
            int index = 0;

            for (int i = 0; i < color.length; ++i) {
                color[i] = (bgr[index + 2] & 255) << 16 | (bgr[index + 1] & 255) << 8 | bgr[index] & 255 | -16777216;
                index += 3;
            }

            return color;
        }
    }

    public static Bitmap alignBitmapForBgr24(Bitmap bitmap) {
        if (bitmap != null && bitmap.getWidth() >= 4) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if ((width & 3) != 0) {
                width &= -4;
                bitmap = cropImage(bitmap, new Rect(0, 0, width, height));
            }

            return bitmap;
        } else {
            return null;
        }
    }

    public static Bitmap cropImage(Bitmap bitmap, Rect rect) {
        return bitmap != null && rect != null && !rect.isEmpty() && bitmap.getWidth() >= rect.right && bitmap.getHeight() >= rect.bottom ? Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height(), (Matrix) null, false) : null;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, float rotateDegree) {
        if (bitmap == null) {
            return null;
        } else {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotateDegree);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        }
    }

    public static byte[] cropNv21(byte[] nv21, int width, int height, Rect rect) {
        if (nv21 != null && nv21.length != 0 && width * height * 3 / 2 == nv21.length && rect != null) {
            if (rect.left >= 0 && rect.top >= 0 && rect.right <= width && rect.bottom <= height) {
                if (rect.right > rect.left && rect.bottom > rect.top) {
                    if ((rect.right - rect.left & 1) != 1 && (rect.bottom - rect.top & 1) != 1) {
                        int cropImageWidth = rect.right - rect.left;
                        int cropImageHeight = rect.bottom - rect.top;
                        byte[] cropNv21 = new byte[cropImageWidth * cropImageHeight * 3 / 2];
                        int originalYLineStart = rect.top * width;
                        int targetYIndex = 0;
                        int originalUVLineStart = width * height + rect.top * width / 2;
                        int targetUVIndex = cropImageWidth * cropImageHeight;

                        for (int i = rect.top; i < rect.bottom; ++i) {
                            System.arraycopy(nv21, originalYLineStart + rect.left, cropNv21, targetYIndex, cropImageWidth);
                            originalYLineStart += width;
                            targetYIndex += cropImageWidth;
                            if ((i & 1) == 0) {
                                System.arraycopy(nv21, originalUVLineStart + rect.left, cropNv21, targetUVIndex, cropImageWidth);
                                originalUVLineStart += width;
                                targetUVIndex += cropImageWidth;
                            }
                        }

                        return cropNv21;
                    } else {
                        throw new IllegalArgumentException("nv21 width and height must be even!");
                    }
                } else {
                    throw new IllegalArgumentException("invalid rect!");
                }
            } else {
                throw new IllegalArgumentException("rect out of bounds!");
            }
        } else {
            throw new IllegalArgumentException("invalid image params!");
        }
    }

    public static byte[] cropBgr24(byte[] bgr24, int width, int height, Rect rect) {
        if (bgr24 != null && bgr24.length != 0 && width * height * 3 == bgr24.length && rect != null) {
            if (rect.left >= 0 && rect.top >= 0 && rect.right <= width && rect.bottom <= height) {
                if (rect.right > rect.left && rect.bottom > rect.top) {
                    int cropImageWidth = rect.right - rect.left;
                    int cropImageHeight = rect.bottom - rect.top;
                    byte[] cropBgr24 = new byte[cropImageWidth * cropImageHeight * 3];
                    int originalLineStart = rect.top * width * 3;
                    int targetIndex = 0;

                    for (int i = rect.top; i < rect.bottom; ++i) {
                        System.arraycopy(bgr24, originalLineStart + rect.left * 3, cropBgr24, targetIndex, cropImageWidth * 3);
                        originalLineStart += width * 3;
                        targetIndex += cropImageWidth * 3;
                    }

                    return cropBgr24;
                } else {
                    throw new IllegalArgumentException("invalid rect!");
                }
            } else {
                throw new IllegalArgumentException("rect out of bounds!");
            }
        } else {
            throw new IllegalArgumentException("invalid image params!");
        }
    }

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
//                a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
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
}

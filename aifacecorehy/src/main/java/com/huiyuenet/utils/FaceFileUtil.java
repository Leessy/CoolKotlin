package com.huiyuenet.utils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FaceFileUtil {

    public static void saveImage(Bitmap image, String path, String fileName){
        if(image == null){
            return;
        }

        try {
            File file = new File(path, fileName);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch(Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    public static void saveData(byte[] data, String filePath, String fileName) {
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //File pictureFile = new File(dir, fileName);
        File pictureFile = new File(dir, fileName);
        FileOutputStream fOut = null;
        try {

//    		byte[] bitmap = convertToBitmapArray(data, imageH,imageW);
            fOut = new FileOutputStream(pictureFile);
            fOut.write(data, 0, data.length);
            fOut.close();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (null != fOut) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

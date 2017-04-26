package digimatic.shangcommerce.testmode.Util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Copyright (c) 2016, Stacck Pte Ltd. All rights reserved.
 *
 * @author Lio <lphan@stacck.com>
 * @version 1.0
 * @since September 29, 2016
 */

public class ImageUtil {

    public static String FOLDER = "Test";
    public static String FILENAME = "image";
    public static String EXTENSION = "jpg";
    public static String FULL_NAME = "image.jpg";

    public static Uri createTempFile(Context context) {
        deleteTempFile(context);
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + FOLDER);

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        File file = new File(myDir, FULL_NAME);
        Uri uri = Uri.fromFile(file);

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

        return uri;
    }

    public static void deleteTempFile(Context context) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + FOLDER);

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        File file = new File(myDir, FULL_NAME);
        file.delete();

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    public static Uri getTempFile(Context context) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + FOLDER);

        File file = new File(myDir, FULL_NAME);
        Uri uri = Uri.fromFile(file);

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

        return uri;
    }

    public static Uri saveBitmapToSDCard(Context context, Bitmap bitmap, Uri uri) {
        File file = new File(uri.getPath());
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return uri;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

        }
    }
}

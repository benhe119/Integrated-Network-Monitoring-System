package automan.automanclientsmobile_android.CONTROLLER;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.R;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Kwadwo AmoAddai
 */
public class ImageLoader {

    // the simplest in-memory cache implementation. This should be replaced with
    // something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
    private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();

    private File cacheDir;

    private Boolean DBmode = false;
    private AutomanEnumerations sth = null;

    public ImageLoader(AutomanEnumerations sth, Context context, Boolean dbmode) {
        // Make the background thread low priority. This way it will not affect
        // the UI performance

        this.sth = sth;
        this.DBmode = false;
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);

        // Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "LazyList");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    final int stub_id = R.drawable.ic_image_loading;

    public void displayImage(String img_stub, Activity activity, ImageView imageView) {
        if (cache.containsKey(img_stub))
            imageView.setImageBitmap(cache.get(img_stub));
        else {
            queuePhoto(img_stub, activity, imageView);
            imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String img_stub, Activity activity, ImageView imageView) {
        // This ImageView may be used for other images before. So there may be
        // some old tasks in the queue. We need to discard them.
        photosQueue.Clean(imageView);
        PhotoToLoad p = new PhotoToLoad(img_stub, imageView);
        synchronized (photosQueue.photosToLoad) {
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }

        // start thread if it's not started yet
        if (photoLoaderThread.getState() == Thread.State.NEW)
            photoLoaderThread.start();
    }

    private Bitmap getBitmap(String img_stub) {
        // I identify images by hashcode. Not a perfect solution, good for the
        // demo.
        Bitmap bitmap = null;
        String filename = String.valueOf(img_stub.hashCode());
        File f = new File(cacheDir, filename);

        // from SD cache
        bitmap = decodeFile(f);
        if (bitmap != null) return bitmap;

        try {
            if (this.DBmode) { // img_stub FOR API IS NOT SAME AS filename FOR PERSONAL DATABASE
//                f = AutomanApp.getApp().db().retrieveFile(img_stub, this.sth, ".png"); // FIND A WAY TO MAKE THIS ".png" FILETYPE GENERIC
            } else {
                // from web
                InputStream is = new URL(AutomanApp.getApp().imgurl() + img_stub).openStream();
                OutputStream os = new FileOutputStream(f);
                byte[] buff = new byte[1024];
                int len = is.read(buff);
                while (len != -1) {
                    os.write(buff, 0, len);
                    len = is.read(buff);
                }
                os.close();
            }
            bitmap = decodeFile(f);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return bitmap;
    }

    // decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    // Task for the queue
    private class PhotoToLoad {
        public String img_stub;
        public ImageView imageView;

        public PhotoToLoad(String img_stub, ImageView i) {
            this.img_stub = img_stub;
            this.imageView = i;
        }
    }

    PhotosQueue photosQueue = new PhotosQueue();

    public void stopThread() {
        photoLoaderThread.interrupt();
    }

    // stores list of photos to download
    class PhotosQueue {
        private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

        // removes all instances of this ImageView
        public void Clean(ImageView image) {
            for (int j = 0; j < photosToLoad.size(); ) {
                if (photosToLoad.get(j).imageView == image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
    }

    class PhotosLoader extends Thread {
        public void run() {
            try {
                while (true) {
                    // thread waits until there are any images to load in the
                    // queue
                    if (photosQueue.photosToLoad.size() == 0)
                        synchronized (photosQueue.photosToLoad) {
                            photosQueue.photosToLoad.wait();
                        }
                    if (photosQueue.photosToLoad.size() != 0) {
                        PhotoToLoad photoToLoad;
                        synchronized (photosQueue.photosToLoad) {
                            photoToLoad = photosQueue.photosToLoad.pop(); // PICK OUT FIRST PHOTO (FIFO)
                        }
                        Bitmap bmp = getBitmap(photoToLoad.img_stub);
                        cache.put(photoToLoad.img_stub, bmp);
                        if ((photoToLoad.imageView.getTag().toString())
                                .equals(photoToLoad.img_stub)) {
                            BitmapDisplayer bd = new BitmapDisplayer(bmp,
                                    photoToLoad.imageView);
                            Activity a = (Activity) photoToLoad.imageView
                                    .getContext();
                            a.runOnUiThread(bd);
                        }
                    }
                    if (Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                // allow thread to exit
            }
        }
    }

    PhotosLoader photoLoaderThread = new PhotosLoader();

    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        ImageView imageView;

        public BitmapDisplayer(Bitmap b, ImageView i) {
            bitmap = b;
            imageView = i;
        }

        public void run() {
            if (bitmap != null)
                imageView.setImageBitmap(bitmap);
            else
                imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        // clear memory cache
        cache.clear();

        // clear SD cache
        File[] files = cacheDir.listFiles();
        for (File f : files)
            f.delete();
    }

}

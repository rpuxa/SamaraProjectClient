package ru.samara.mapapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public final class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private DownloadImageTask() {
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception ignored) {
        }
        return mIcon11;
    }

    public static Bitmap getImage(String url) {
        try {
            return new DownloadImageTask().execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Fail");
    }

}
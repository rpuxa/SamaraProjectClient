package ru.samara.mapapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import ru.samara.mapapp.R;

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

    public static Bitmap downloadInBackground(Context context, String url, DownloadProgressListener listener) {
        final Bitmap[] bitmap = {BitmapFactory.decodeResource(context.getResources(), R.drawable.other)};
        new Thread(() -> {
            bitmap[0] = getImage(url);
            if (listener != null)
                listener.onComplete(bitmap[0]);
        }).start();
        return bitmap[0];
    }

    public interface DownloadProgressListener {
        void onComplete(Bitmap bitmap);
    }

}
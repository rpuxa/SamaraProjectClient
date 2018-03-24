package ru.samara.mapapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
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
            return getClip(new DownloadImageTask().execute(url).get());
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

    public static Bitmap getClip(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2,
                bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

}
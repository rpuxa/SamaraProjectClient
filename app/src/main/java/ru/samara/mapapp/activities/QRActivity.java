package ru.samara.mapapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import ru.samara.mapapp.R;
import ru.samara.mapapp.qr.IntentIntegrator;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class QRActivity extends AppCompatActivity {

    public static final String QR_CODE = "qrcd";

    private static final int SIZE = 512;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_qrcode);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String text = bundle.getString(QR_CODE);
        Bitmap bitmap = create(text);
        ((ImageView) findViewById(R.id.qr_code_image_view)).setImageBitmap(bitmap);
    }

    public static void show(Activity activity, String text) {
        Intent intent = new Intent(activity, QRActivity.class);
        intent.putExtra(QR_CODE, text);
        activity.startActivity(intent);
    }

    public static void read(Activity activity) {
        new IntentIntegrator(activity).initiateScan();
    }

    private static Bitmap create(String str) {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, SIZE, SIZE, null);
        } catch (IllegalArgumentException iae) {

            return null;
        } catch (WriterException e) {
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, SIZE, 0, 0, w, h);

        return bitmap;
    }


}

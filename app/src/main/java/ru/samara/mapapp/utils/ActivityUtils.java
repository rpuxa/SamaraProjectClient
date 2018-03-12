package ru.samara.mapapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public final class ActivityUtils {
    private ActivityUtils() {
    }

    public static void changeVisible(View view) {
        if (view.getVisibility() == View.GONE)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
    }

    public static void changeActivity(Activity from, Class<? extends Activity> to) {
        Intent intent = new Intent(from, to);
        from.startActivity(intent);
    }

}

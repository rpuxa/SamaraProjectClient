package ru.samara.mapapp;

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
}

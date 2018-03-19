package ru.samara.mapapp.activities;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

public abstract class Content {

    @LayoutRes
    private int layout;
    private ViewGroup mainGroup;
    private MainActivity parent;

    protected Content() {
    }

    public abstract void onCreate(MainActivity parent, Intent intent);

    public abstract int layout();

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public final void setParent(MainActivity parent) {
        this.parent = parent;
    }

    public final void setLayout(int layout) {
        this.layout = layout;
    }

    public final int getLayout() {
        return layout;
    }

    public MainActivity getParent() {
        return parent;
    }

    void setMainGroup(ViewGroup mainGroup) {
        this.mainGroup = mainGroup;
    }

    public final View findViewById(@IdRes int id) {
        return mainGroup.findViewById(id);
    }
}

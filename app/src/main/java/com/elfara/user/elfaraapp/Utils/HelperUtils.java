package com.elfara.user.elfaraapp.Utils;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.elfara.user.elfaraapp.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HelperUtils {
    private AppCompatActivity activity;
    private Context context;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public HelperUtils(AppCompatActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.SCREEN_WIDTH = displayMetrics.widthPixels;
        this.SCREEN_HEIGHT = displayMetrics.heightPixels;
    }

    public void changeFragment(Fragment newFragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_content, newFragment);
        transaction.addToBackStack("tag");
        transaction.commit();
    }
}

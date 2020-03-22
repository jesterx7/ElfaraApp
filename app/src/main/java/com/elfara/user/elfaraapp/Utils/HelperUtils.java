package com.elfara.user.elfaraapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.elfara.user.elfaraapp.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.ResponseBody;

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

    public void downloadTextFile(ResponseBody body, String filename, String ext) {
        try {
            File path = Environment.getExternalStorageDirectory();
            File file = new File(path, filename + ext);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            IOUtils.write(body.bytes(), fileOutputStream);
            Toast.makeText(context, "Data Saved as " + filename + ext, Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            System.out.println(ex.getStackTrace());
            Toast.makeText(context, "Download Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void downloadImageFile(ResponseBody body, String filename) {
        try {
            File path = Environment.getExternalStorageDirectory();
            File file = new File(path, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            IOUtils.write(body.bytes(), fileOutputStream);
            Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri fileUri = Uri.fromFile(file);
            mediaScan.setData(fileUri);
            activity.sendBroadcast(mediaScan);
            Toast.makeText(context, "Data Saved as " + filename, Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            System.out.println(ex.getStackTrace());
            Toast.makeText(context, "Download Failed", Toast.LENGTH_SHORT).show();
        }
    }
}

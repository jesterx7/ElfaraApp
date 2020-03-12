package com.elfara.user.elfaraapp.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsUtils {
    private Activity activity;
    private Context context;

    public PermissionsUtils(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public boolean read_media_permissions() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);

            return false;
        }
        return true;
    }
}

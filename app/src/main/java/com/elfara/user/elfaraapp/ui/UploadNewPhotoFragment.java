package com.elfara.user.elfaraapp.ui;


import android.Manifest;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.elfara.user.elfaraapp.Adapter.ImageAdapter;
import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Model.Session;
import com.elfara.user.elfaraapp.Model.UploadResponse;
import com.elfara.user.elfaraapp.R;
import com.elfara.user.elfaraapp.Utils.ImageUtils;
import com.elfara.user.elfaraapp.Utils.PermissionsUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadNewPhotoFragment extends Fragment {
    private ImageUtils imageUtils;
    private PermissionsUtils permissionsUtils;
    private View view;
    private Button btnUpload;
    private ViewPager viewPager;
    private ProgressBar progressBar;
    private Session session;

    private static final int GET_FROM_GALLERY = 3;

    public UploadNewPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload_new_photo, container, false);
        session = new Session(getContext());

        viewPager = view.findViewById(R.id.viewPagerUploadNewPhoto);
        btnUpload = view.findViewById(R.id.btnUploadUploadNewPhoto);
        progressBar = view.findViewById(R.id.progressBarUploadNewPhoto);

        imageUtils = new ImageUtils(getContext());
        permissionsUtils = new PermissionsUtils(getActivity(), getContext());

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<Uri> imageUriList = new ArrayList<>();
        if (resultCode == getActivity().RESULT_OK) {
            progressBar.setVisibility(View.VISIBLE);
            if (data.getClipData() == null) {
                imageUriList.add(data.getData());
                ImageAdapter imageAdapter = new ImageAdapter(getContext(), imageUriList);
                viewPager.setAdapter(imageAdapter);
                if (permissionsUtils.read_media_permissions()) {
                    System.out.println("TESTING");
                    uploadImage(imageUriList);
                }
            } else {
                ClipData clipData = data.getClipData();
                for (int i=0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    imageUriList.add(item.getUri());
                    ImageAdapter imageAdapter = new ImageAdapter(getContext(), imageUriList);
                    viewPager.setAdapter(imageAdapter);
                    if (permissionsUtils.read_media_permissions()) {
                        uploadImage(imageUriList);
                    }
                }
            }
        }
    }

    private MultipartBody.Part buildImageFiles(ArrayList<Uri> imageUriList) {
        /*MultipartBody.Part[] parts = new MultipartBody.Part[imageUriList.size()];
        for (int i=0; i < imageUriList.size(); i++) {
            File file = new File(imageUriList.get(i).getPath());
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            parts[i] = MultipartBody.Part.createFormData("images", file.getName(), requestBody);
            System.out.println("FILE " + (i+1) + " : " + file);
        }*/
        File file = new File(imageUriList.get(0).getPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("images", file.getName(), requestBody);
        return parts;
    }

    private void uploadImage(ArrayList<Uri> imgUriList) {
        File file = new File(imageUtils.getPathFromUri(getContext(), imgUriList.get(0)));
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        String filename = queryName(getContext().getContentResolver(), imgUriList.get(0));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", filename, requestBody);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UploadResponse> uploadResponse = apiInterface.uploadImage(
                Integer.parseInt(session.getSession("iduser")), filePart
        );

        uploadResponse.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body().getSuccess()) {
                    System.out.println("SUCCESS : " + response.body().getMessage());
                } else {
                    System.out.println("FAILED  : " + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                System.out.println("CALL BACK : " + call);
                System.out.println("ERROR : " + t.getMessage());
            }
        });
    }



    private String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }
}

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
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Adapter.ImageAdapter;
import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Model.Session;
import com.elfara.user.elfaraapp.Model.UploadResponse;
import com.elfara.user.elfaraapp.R;
import com.elfara.user.elfaraapp.Utils.ImageUtils;
import com.elfara.user.elfaraapp.Utils.PermissionsUtils;
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
    private ArrayList<Uri> imageUriList;

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
                imageUriList = new ArrayList<>();
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
        if (resultCode == getActivity().RESULT_OK) {
            progressBar.setVisibility(View.VISIBLE);
            if (data.getClipData() == null) {
                imageUriList.add(data.getData());
                ImageAdapter imageAdapter = new ImageAdapter(getContext(), imageUriList);
                viewPager.setAdapter(imageAdapter);
                if (permissionsUtils.read_media_permissions()) {
                    if (imageUtils.checkExtensionImages(imageUriList)) {
                        uploadImage(imageUriList);
                    } else {
                        Toast.makeText(getContext(), "Image JPG and PNG Only!", Toast.LENGTH_SHORT);
                    }
                }
            } else {
                ClipData clipData = data.getClipData();
                for (int i=0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    imageUriList.add(item.getUri());
                    ImageAdapter imageAdapter = new ImageAdapter(getContext(), imageUriList);
                    viewPager.setAdapter(imageAdapter);
                }
                if (permissionsUtils.read_media_permissions()) {
                    if (imageUtils.checkExtensionImages(imageUriList)) {
                        uploadImage(imageUriList);
                    } else {
                        Toast.makeText(getContext(), "Image JPG and PNG Only!", Toast.LENGTH_SHORT);
                    }
                }
            }
        }
    }

    private void uploadImage(ArrayList<Uri> imgUriList) {
        MultipartBody.Part[] parts = imageUtils.buildImageFiles(imgUriList);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UploadResponse> uploadResponse = apiInterface.uploadImage(
                Integer.parseInt(session.getSession("iduser")), parts
        );

        uploadResponse.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body().getSuccess()) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error When Upload Images", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

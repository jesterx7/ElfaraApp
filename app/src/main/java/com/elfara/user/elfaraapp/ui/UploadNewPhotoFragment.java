package com.elfara.user.elfaraapp.ui;


import android.Manifest;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Adapter.ImageAdapter;
import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Model.Session;
import com.elfara.user.elfaraapp.Model.UploadResponse;
import com.elfara.user.elfaraapp.R;
import com.elfara.user.elfaraapp.Utils.HelperUtils;
import com.elfara.user.elfaraapp.Utils.ImageUtils;
import com.elfara.user.elfaraapp.Utils.PermissionsUtils;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadNewPhotoFragment extends Fragment {
    private ImageUtils imageUtils;
    private PermissionsUtils permissionsUtils;
    private HelperUtils helper;
    private View view;
    private Button btnUpload, btnSubmit;
    private ViewPager viewPager;
    private ImageView ivIcon;
    private TextView tvUpload;
    private ProgressBar progressBar;
    private Session session;
    private ArrayList<Uri> imageUriList;
    private boolean uploadStatus;

    public UploadNewPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view = inflater.inflate(R.layout.fragment_upload_new_photo, container, false);
        session = new Session(getContext());

        viewPager = view.findViewById(R.id.viewPagerUploadNewPhoto);
        ivIcon = view.findViewById(R.id.ivIconUploadNewPhoto);
        tvUpload = view.findViewById(R.id.tvUploadUploadNewPhoto);
        btnUpload = view.findViewById(R.id.btnUploadUploadNewPhoto);
        btnSubmit = view.findViewById(R.id.btnSubmitPhotoUploadNewPhoto);
        progressBar = view.findViewById(R.id.progressBarUploadNewPhoto);

        imageUtils = new ImageUtils(getContext());
        permissionsUtils = new PermissionsUtils(getActivity(), getContext());
        helper = new HelperUtils((AppCompatActivity)getActivity(), getContext());
        uploadStatus = false;

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                imageUriList = new ArrayList<>();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uploadStatus) {
                    progressBar.setVisibility(View.VISIBLE);
                    uploadImage(imageUriList);
                } else {
                    Toast.makeText(getContext(), "Image cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Upload Photo");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (data.getClipData() == null) {
                imageUriList.add(data.getData());
                ImageAdapter imageAdapter = new ImageAdapter(getContext(), imageUriList);
                viewPager.setAdapter(imageAdapter);
                ivIcon.setVisibility(View.GONE);
                tvUpload.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
                if (permissionsUtils.read_media_permissions()) {
                    if (!imageUtils.checkExtensionImages(imageUriList)) {
                        Toast.makeText(getContext(), "Image JPG and PNG Only!", Toast.LENGTH_SHORT);
                    } else {
                        uploadStatus = true;
                    }
                }
            } else {
                ClipData clipData = data.getClipData();
                for (int i=0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    imageUriList.add(item.getUri());
                    ImageAdapter imageAdapter = new ImageAdapter(getContext(), imageUriList);
                    viewPager.setAdapter(imageAdapter);
                    ivIcon.setVisibility(View.GONE);
                    tvUpload.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                }
                if (permissionsUtils.read_media_permissions()) {
                    if (!imageUtils.checkExtensionImages(imageUriList)) {
                        Toast.makeText(getContext(), "Image JPG and PNG Only!", Toast.LENGTH_SHORT);
                    } else {
                        uploadStatus = true;
                    }
                }
            }
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                if (response.isSuccessful() && response.body().getSuccess()) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    helper.changeFragment(new UploadPhotoFragment());
                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error When Upload Images", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

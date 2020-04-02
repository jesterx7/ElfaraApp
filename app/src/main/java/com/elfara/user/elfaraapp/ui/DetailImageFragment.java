package com.elfara.user.elfaraapp.ui;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.R;
import com.elfara.user.elfaraapp.Utils.HelperUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailImageFragment extends Fragment {
    private View view;
    private HelperUtils helper;
    private ImageView imgDetailImage;
    private TextView tvUploadedBy;
    private ProgressBar progressBar;
    private TextView tvDownload;
    private Button btnBackDetailImage, btnDownloadImage;
    private String urlImage, imgName, uploadAt, username;
    private ApiInterface apiInterface;


    public DetailImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_image, container, false);
        helper = new HelperUtils((AppCompatActivity)getActivity(), getContext());
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        imgDetailImage = view.findViewById(R.id.imgDetailImage);
        tvUploadedBy = view.findViewById(R.id.tvUploadedByDetailImage);
        btnBackDetailImage = view.findViewById(R.id.btnBackDetailImage);
        btnDownloadImage = view.findViewById(R.id.btnDownloadDetailImage);
        progressBar = view.findViewById(R.id.progressBarDetailImage);
        tvDownload = view.findViewById(R.id.tvDownloadDetailImage);

        urlImage = getArguments().getString("urlImage");
        imgName = getArguments().getString("imgName");
        uploadAt = getArguments().getString("uploadAt");
        username = getArguments().getString("username");

        Glide.with(getContext())
                .load(urlImage)
                .signature(new ObjectKey(uploadAt))
                .into(imgDetailImage);
        tvUploadedBy.setText(username);
        progressBar.setVisibility(View.GONE);

        btnBackDetailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnDownloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgDetailImage.setVisibility(View.GONE);
                tvDownload.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                Call<ResponseBody> call = apiInterface.downloadImage(
                        imgName
                );
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                helper.downloadImageFile(response.body(), imgName);
                            } else {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                                helper.downloadImageFile(response.body(), imgName);
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to Download Image", Toast.LENGTH_SHORT).show();
                        }
                        imgDetailImage.setVisibility(View.VISIBLE);
                        tvDownload.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "Error when download : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        imgDetailImage.setVisibility(View.VISIBLE);
                        tvDownload.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Photo Detail");
    }

}

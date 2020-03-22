package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.MainActivity;
import com.elfara.user.elfaraapp.Model.UrlResponse;
import com.elfara.user.elfaraapp.R;
import com.elfara.user.elfaraapp.Utils.HelperUtils;
import com.elfara.user.elfaraapp.Utils.ImageUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoListFragment extends Fragment {
    private View view;
    private HelperUtils helper;
    private ImageUtils imageUtils;
    private ApiInterface apiInterface;
    private ProgressBar progressBar;
    private LinearLayout llPhotoList;
    private int idevent;
    private String dateFrom, dateTo, namaEvent;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .circleCropTransform()
            .placeholder(R.drawable.img_loading)
            .error(R.mipmap.ic_launcher_round)
            .transform(new CenterCrop(), new RoundedCorners(16));


    public PhotoListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.fragment_photo_list, container, false);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        helper = new HelperUtils((AppCompatActivity)getActivity(), getContext());
        imageUtils = new ImageUtils(getContext());
        progressBar = view.findViewById(R.id.progressBarPhotoList);
        llPhotoList = view.findViewById(R.id.llPhotoList);

        idevent = Integer.parseInt(getArguments().getString("idevent","1"));
        namaEvent = getArguments().getString("nama_event", "default");
        dateFrom = getArguments().getString("dateFrom", "");
        dateTo = getArguments().getString("dateTo", "");

        getListImagesUrl();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_nav_download_button, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuDownloadZipImages) {
            Call<ResponseBody> call = apiInterface.getZipImages(
                    idevent, namaEvent, dateFrom, dateTo
            );
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.VISIBLE);
                        helper.downloadTextFile(response.body(), namaEvent, ".zip");
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getContext(), "Failed to Download", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Error when Download : " + t.getMessage() , Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getListImagesUrl() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UrlResponse> call = apiInterface.getUrl(
                idevent, dateFrom, dateTo
        );

        call.enqueue(new Callback<UrlResponse>() {
            @Override
            public void onResponse(Call<UrlResponse> call, Response<UrlResponse> response) {
                if (response.isSuccessful() && response.body().getSuccess() && !response.body().getUrlList().equals("null")) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<String>>(){}.getType();
                    List<String> urlList = gson.fromJson(response.body().getUrlList(), listType);
                    List<String> filenameList = gson.fromJson(response.body().getFilenameList(), listType);
                    buildPhotoList(urlList, filenameList);
                } else {
                    Toast.makeText(getContext(), "No Image Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UrlResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error when get Images", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buildPhotoList(final List<String> urlList, final List<String> filenameList) {
        int count = 0;
        final int width, height;
        width = height = helper.SCREEN_WIDTH / 2 - 10;

        LinearLayout linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        while (count < urlList.size()) {
            final ImageView imageView = new ImageView(getContext());
            final int finalCount = count;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("urlImage", urlList.get(finalCount));
                    bundle.putString("imgName", filenameList.get(finalCount));
                    Fragment fragment = new DetailImageFragment();
                    fragment.setArguments(bundle);
                    helper.changeFragment(fragment);
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            params.weight = 1;
            Glide.with(getContext())
                    .load(urlList.get(count))
                    .apply(options)
                    .into(imageView);
            if (count % 2 == 0) {
                linearLayout = new LinearLayout(getContext());
                llPhotoList.addView(linearLayout);
                params.setMargins(0, 0, 10, 25);
                imageView.setLayoutParams(params);
                linearLayout.addView(imageView);
                if (count == urlList.size()-1)
                    linearLayout.addView(imageUtils.createBlankImageView(width, height));
            } else {
                params.setMargins(10, 0, 0, 25);
                imageView.setLayoutParams(params);
                linearLayout.addView(imageView);
            }
            count++;
        }
    }
}

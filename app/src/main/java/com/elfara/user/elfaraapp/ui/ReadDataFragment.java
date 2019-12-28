package com.elfara.user.elfaraapp.ui;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Adapter.ReadDataAdapter;
import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.MainActivity;
import com.elfara.user.elfaraapp.Model.ReadData;
import com.elfara.user.elfaraapp.R;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReadDataFragment extends Fragment {
    private View view;
    private TextView tvNoData;
    private RecyclerView recyclerView;
    private Button btnDownload;
    private ProgressBar progressBar;
    private ArrayList<ReadData> readDataArrayList;
    private String inputDateFrom, inputDateTo;


    public ReadDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_read_data, container, false);

        tvNoData = view.findViewById(R.id.tvNoDataReadData);
        recyclerView = view.findViewById(R.id.rvReadData);
        btnDownload = view.findViewById(R.id.btnDownloadReadData);
        progressBar = view.findViewById(R.id.progressBarReadData);

        inputDateFrom = getArguments().getString("dateFrom");
        inputDateTo = getArguments().getString("dateTo");

        getReadData(inputDateFrom, inputDateTo);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                progressBar.setVisibility(View.VISIBLE);
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                apiInterface.downloadExcel().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                downloadData(response.body());
                            } else {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                                downloadData(response.body());
                            }
                        } else {
                            Toast.makeText(view.getContext(), "Failed to Download Data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(view.getContext(), "Dwonload Error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

        return view;
    }

    private void getReadData(String dateFrom, String dateTo) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<ReadData>> readDataCall = apiInterface.getReadData(
                dateFrom, dateTo
        );

        readDataCall.enqueue(new Callback<ArrayList<ReadData>>() {
            @Override
            public void onResponse(Call<ArrayList<ReadData>> call, Response<ArrayList<ReadData>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(view.getContext(), "Success", Toast.LENGTH_SHORT).show();
                    ReadDataAdapter readDataAdapter = new ReadDataAdapter(view.getContext(), response.body());
                    recyclerView.setAdapter(readDataAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(view.getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    tvNoData.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ReadData>> call, Throwable t) {
                Toast.makeText(view.getContext(), "Failed Response", Toast.LENGTH_SHORT).show();
                tvNoData.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void downloadData(ResponseBody body) {
        try {
            File path = Environment.getExternalStorageDirectory();
            File file = new File(path, "report_data.xls");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            IOUtils.write(body.bytes(), fileOutputStream);
            Toast.makeText(view.getContext(), "Download Success", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        catch (Exception ex){
            System.out.println(ex.getStackTrace());
            Toast.makeText(view.getContext(), "Download Failed", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

}

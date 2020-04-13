package com.elfara.user.elfaraapp.ui;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Adapter.ReadDataAdapter;
import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.MainActivity;
import com.elfara.user.elfaraapp.Model.ReadData;
import com.elfara.user.elfaraapp.R;
import com.elfara.user.elfaraapp.Utils.HelperUtils;
import com.elfara.user.elfaraapp.Utils.PermissionsUtils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReadDataFragment extends Fragment {
    private View view;
    private PermissionsUtils permissionsUtils;
    private HelperUtils helper;
    private TextView tvNoData;
    private RecyclerView recyclerView;
    private Button btnDownload;
    private ProgressBar progressBar;
    private String inputDateFrom, inputDateTo;
    private int idEvent;
    private ApiInterface apiInterface;


    public ReadDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view = inflater.inflate(R.layout.fragment_read_data, container, false);
        permissionsUtils = new PermissionsUtils(getActivity(), getContext());
        helper = new HelperUtils((AppCompatActivity)getActivity(), getContext());

        tvNoData = view.findViewById(R.id.tvNoDataReadData);
        recyclerView = view.findViewById(R.id.rvReadData);
        btnDownload = view.findViewById(R.id.btnDownloadReadData);
        progressBar = view.findViewById(R.id.progressBarReadData);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        if (getArguments().getString("idevent") == null) {
            inputDateFrom = getArguments().getString("dateFrom");
            inputDateTo = getArguments().getString("dateTo");
            getReadData(inputDateFrom, inputDateTo);
        } else {
            idEvent = Integer.parseInt(getArguments().getString("idevent"));
            getReadDataByEvent(idEvent);
        }

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!permissionsUtils.isStoragePermissionGranted()) {
                    Toast.makeText(view.getContext(), "Allow to Download Excel!", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Save As");
                    final EditText edtFileName = new EditText(view.getContext());
                    builder.setView(edtFileName);

                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        final String filename = edtFileName.getText().toString();
                        if (!filename.isEmpty()) {
                            progressBar.setVisibility(View.VISIBLE);
                            apiInterface.exportToExcel(inputDateFrom, inputDateTo, filename).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                            helper.downloadTextFile(response.body(), filename, ".xlsx");
                                        } else {
                                            ActivityCompat.requestPermissions(getActivity(),
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                                            helper.downloadTextFile(response.body(), filename, ".xlsx");
                                        }
                                    } else {
                                        Toast.makeText(view.getContext(), "Failed to Download Data", Toast.LENGTH_SHORT).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(view.getContext(), "Download Error", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    builder.show();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Database");
    }

    private void getReadData(String dateFrom, String dateTo) {
        Call<ArrayList<ReadData>> readDataCall = apiInterface.getReadData(
                dateFrom, dateTo
        );

        readDataCall.enqueue(new Callback<ArrayList<ReadData>>() {
            @Override
            public void onResponse(Call<ArrayList<ReadData>> call, Response<ArrayList<ReadData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(view.getContext(), "Success", Toast.LENGTH_SHORT).show();
                    ReadDataAdapter readDataAdapter = new ReadDataAdapter(view.getContext(), response.body());
                    recyclerView.setAdapter(readDataAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(view.getContext(), "No Data Found!!", Toast.LENGTH_SHORT).show();
                    tvNoData.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ReadData>> call, Throwable t) {
                Toast.makeText(view.getContext(), "Failed DefaultResponse", Toast.LENGTH_SHORT).show();
                tvNoData.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getReadDataByEvent(int idEvent) {
        Call<ArrayList<ReadData>> readDataCall = apiInterface.getReadDataByEvent(idEvent);
        readDataCall.enqueue(new Callback<ArrayList<ReadData>>() {
            @Override
            public void onResponse(Call<ArrayList<ReadData>> call, Response<ArrayList<ReadData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(view.getContext(), "Success", Toast.LENGTH_SHORT).show();
                    ReadDataAdapter readDataAdapter = new ReadDataAdapter(view.getContext(), response.body());
                    recyclerView.setAdapter(readDataAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(view.getContext(), "No Data Found!!", Toast.LENGTH_SHORT).show();
                    tvNoData.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ReadData>> call, Throwable t) {
                Toast.makeText(view.getContext(), "Failed DefaultResponse", Toast.LENGTH_SHORT).show();
                tvNoData.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }


}

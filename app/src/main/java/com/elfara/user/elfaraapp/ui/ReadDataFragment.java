package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Adapter.ReadDataAdapter;
import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Model.ReadData;
import com.elfara.user.elfaraapp.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReadDataFragment extends Fragment {
    private View view;
    private TextView tvNoData;
    private RecyclerView recyclerView;
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
        progressBar = view.findViewById(R.id.progressBarReadData);

        inputDateFrom = getArguments().getString("dateFrom");
        inputDateTo = getArguments().getString("dateTo");

        getReadData(inputDateFrom, inputDateTo);

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

}

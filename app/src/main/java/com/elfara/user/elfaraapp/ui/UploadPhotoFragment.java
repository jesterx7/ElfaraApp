package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Model.Event;
import com.elfara.user.elfaraapp.R;
import com.elfara.user.elfaraapp.Utils.HelperUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadPhotoFragment extends Fragment {
    private HelperUtils helper;
    private View view;
    private LinearLayout llMain;
    private ProgressBar progressBar;
    private Spinner spinnerEvent;
    private Button btnUpload, btnSubmit;
    private ApiInterface apiInterface;
    private ArrayList<Event> events;


    public UploadPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload_photo, container, false);

        helper = new HelperUtils((AppCompatActivity)getActivity(), getContext());
        llMain = view.findViewById(R.id.llMainUploadPhoto);
        progressBar = view.findViewById(R.id.progressBarUploadPhoto);
        spinnerEvent = view.findViewById(R.id.spinnerUploadPhoto);
        btnUpload = view.findViewById(R.id.btnUploadUploadPhoto);
        btnSubmit = view.findViewById(R.id.btnSubmitUploadPhoto);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        getEventList();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.changeFragment(new UploadNewPhotoFragment());
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.changeFragment(new PhotoListFragment());
            }
        });

        return view;
    }

    private void getEventList() {
        Call<ArrayList<Event>> call = apiInterface.getEventList();
        call.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if (response.isSuccessful()) {
                    events = response.body();
                    String[] eventList = new String[events.size()];
                    for (int i=0; i<response.body().size(); i++) {
                        eventList[i] = response.body().get(i).getNama();
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_event_title, R.id.tvSpinnerEventTitle, eventList);
                    spinnerEvent.setAdapter(arrayAdapter);
                }
                progressBar.setVisibility(View.GONE);
                llMain.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                llMain.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

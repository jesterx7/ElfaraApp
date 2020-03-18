package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.elfara.user.elfaraapp.Function.FunctionEventLog;
import com.elfara.user.elfaraapp.Model.DefaultResponse;
import com.elfara.user.elfaraapp.Model.Event;
import com.elfara.user.elfaraapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventTitleFragment extends Fragment {
    private View view;
    private LinearLayout llMain;
    private Spinner spinnerEvent;
    private Button btnChange, btnCreate;
    private ProgressBar progressBar;
    private ArrayList<Event> events;
    private FunctionEventLog functionEventLog;
    private ApiInterface apiInterface;


    public EventTitleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_title, container, false);

        llMain = view.findViewById(R.id.llMainEventTitle);
        spinnerEvent = view.findViewById(R.id.spinnerEventTitle);
        btnChange = view.findViewById(R.id.btnChangeEvent);
        btnCreate = view.findViewById(R.id.btnCreateEvent);
        progressBar = view.findViewById(R.id.progressBarEvent);

        functionEventLog = new FunctionEventLog(view.getContext());
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        getEventList();

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeEvent(events.get(spinnerEvent.getSelectedItemPosition()).getIdEvent());
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment fragment = new CreateEventFragment();
                transaction.replace(R.id.frame_content, fragment);
                transaction.addToBackStack("tag");
                transaction.commit();
            }
        });

        return view;
    }

    private void changeEvent(int idEvent) {
        Call<DefaultResponse> call = apiInterface.changeEventTitle(idEvent);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    Toast.makeText(getContext(), "Event Changed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to Change", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEventList() {
        Call<ArrayList<Event>> call = apiInterface.getEventList();
        call.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if (response.isSuccessful()) {
                    events = response.body();
                    String[] eventList = new String[response.body().size()];
                    for (int i=0; i<response.body().size(); i++) {
                        eventList[i] = response.body().get(i).getNama();
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_event_title, R.id.tvSpinnerEventTitle, eventList);
                    spinnerEvent.setAdapter(arrayAdapter);
                }

                progressBar.setVisibility(View.GONE);
                llMain.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                System.out.println("ERROR : " + t.getMessage());
                progressBar.setVisibility(View.GONE);
                llMain.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Event Title");
    }
}

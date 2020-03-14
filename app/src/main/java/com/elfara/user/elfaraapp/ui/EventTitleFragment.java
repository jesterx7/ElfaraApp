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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Function.FunctionEventLog;
import com.elfara.user.elfaraapp.Model.Event;
import com.elfara.user.elfaraapp.Model.EventResponse;
import com.elfara.user.elfaraapp.R;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventTitleFragment extends Fragment {
    private View view;
    private Spinner spinnerEvent;
    private Button btnChange, btnCreate;
    private ProgressBar progressBar;
    private FunctionEventLog functionEventLog;


    public EventTitleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_title, container, false);

        spinnerEvent = view.findViewById(R.id.spinnerEventTitle);
        btnChange = view.findViewById(R.id.btnChangeEvent);
        btnCreate = view.findViewById(R.id.btnCreateEvent);
        progressBar = view.findViewById(R.id.progressBarEvent);

        functionEventLog = new FunctionEventLog(view.getContext());
        getEventList();

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void getEventList() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<EventResponse> call = apiInterface.getEventList();
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    Gson gson = new Gson();
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Event Title");
    }

    private void updateEventTitle(final String newTitle) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Event> eventCall = apiInterface.changeEventTitle(newTitle);

        eventCall.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    functionEventLog.writeEventLog("Update Event Title to " + newTitle);
                    Toast.makeText(view.getContext(), "Title Changed Successfully", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(view.getContext(), "Failed to Change Title", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(view.getContext(), "Error to Change Title", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}

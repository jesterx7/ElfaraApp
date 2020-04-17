package com.elfara.user.elfaraapp.ui;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import com.elfara.user.elfaraapp.Function.FunctionEventLog;
import com.elfara.user.elfaraapp.Model.DefaultResponse;
import com.elfara.user.elfaraapp.Model.Event;
import com.elfara.user.elfaraapp.R;
import com.elfara.user.elfaraapp.Utils.HelperUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventTitleFragment extends Fragment {
    private View view;
    private LinearLayout llMain;
    private Spinner spinnerEvent;
    private Button btnChange, btnCreate, btnEdit, btnDelete;
    private ProgressBar progressBar;
    private ArrayList<Event> events;
    private HelperUtils helper;
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
        btnEdit = view.findViewById(R.id.btnEditEvent);
        btnDelete = view.findViewById(R.id.btnDeleteEvent);
        progressBar = view.findViewById(R.id.progressBarEvent);

        functionEventLog = new FunctionEventLog(view.getContext());
        helper = new HelperUtils((AppCompatActivity)getActivity(), getContext());
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
                helper.changeFragment(new CreateEventFragment());
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you want to delete this Event ? ");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (spinnerEvent.getSelectedItemPosition() == 0) {
                                    Toast.makeText(getContext(), "Please Set Another Event Before Delete", Toast.LENGTH_SHORT).show();
                                } else {
                                    functionEventLog.writeEventLog("Deleted Event " + events.get(spinnerEvent.getSelectedItemPosition()).getNama());
                                    deleteEvent();
                                }
                            }
                        }
                );

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }
                );

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("idevent", events.get(spinnerEvent.getSelectedItemPosition()).getIdEvent());
                bundle.putString("event_name", events.get(spinnerEvent.getSelectedItemPosition()).getNama());
                EditEventFragment editEventFragment = new EditEventFragment();
                editEventFragment.setArguments(bundle);
                helper.changeFragment(editEventFragment);
            }
        });

        return view;
    }

    private void deleteEvent() {
        Call<DefaultResponse> call = apiInterface.deleteEvent(
                events.get(spinnerEvent.getSelectedItemPosition()).getIdEvent()
        );
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    helper.changeFragment(new EventTitleFragment());
                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error when delete : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeEvent(int idEvent) {
        Call<DefaultResponse> call = apiInterface.changeEventTitle(idEvent);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    Toast.makeText(getContext(), "Event Changed", Toast.LENGTH_SHORT).show();
                    helper.changeFragment(new EventTitleFragment());
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

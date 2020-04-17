package com.elfara.user.elfaraapp.ui;


import android.app.DatePickerDialog;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Function.FunctionEventLog;
import com.elfara.user.elfaraapp.Model.Event;
import com.elfara.user.elfaraapp.R;
import com.elfara.user.elfaraapp.Utils.HelperUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DateReadData extends Fragment {
    private View view;
    private ProgressBar progressBar;
    private LinearLayout llMain, llDateForm;
    private Spinner spinnerEvent;
    private Switch switchDate;
    private HelperUtils helper;
    private EditText edtDateFrom, edtDateTo;
    private Button btnSubmit;
    private FunctionEventLog functionEventLog;
    private ApiInterface apiInterface;

    private ArrayList<Event> events;
    private final Calendar calendar = Calendar.getInstance();
    private final String DATEFORMATINPUT = "YYYY-MM-dd";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATEFORMATINPUT, Locale.US);
    private DatePickerDialog.OnDateSetListener dateSetListenerFrom = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH,i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);
            edtDateFrom.setText(simpleDateFormat.format(calendar.getTime()));
        }
    };
    private DatePickerDialog.OnDateSetListener dateSetListenerTo = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH,i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);
            edtDateTo.setText(simpleDateFormat.format(calendar.getTime()));
        }
    };


    public DateReadData() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_date_read_data, container, false);
        helper = new HelperUtils((AppCompatActivity)getActivity(), getContext());

        llMain = view.findViewById(R.id.llInputDateReadData);
        llDateForm = view.findViewById(R.id.llDateFormReadData);
        progressBar = view.findViewById(R.id.progressBarDateReadData);
        spinnerEvent = view.findViewById(R.id.spinnerReadData);
        switchDate = view.findViewById(R.id.switchReadData);
        edtDateFrom = view.findViewById(R.id.edtDateFromReadData);
        edtDateTo = view.findViewById(R.id.edtDateToReadData);
        btnSubmit = view.findViewById(R.id.btnSubmitReadData);

        functionEventLog = new FunctionEventLog(view.getContext());
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        getEventList();

        switchDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    llDateForm.setVisibility(View.VISIBLE);
                } else {
                    llDateForm.setVisibility(View.GONE);
                }
            }
        });

        edtDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), dateSetListenerFrom, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edtDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), dateSetListenerTo, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchDate.isChecked()) {
                    functionEventLog.writeEventLog("Open Database From " + edtDateFrom.getText().toString() + " To " + edtDateTo.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putString("dateFrom", edtDateFrom.getText().toString());
                    bundle.putString("dateTo", edtDateTo.getText().toString());
                    Fragment fragment = new ReadDataFragment();
                    fragment.setArguments(bundle);
                    helper.changeFragment(fragment);
                } else {
                    functionEventLog.writeEventLog("Open Database " + events.get(spinnerEvent.getSelectedItemPosition()).getNama());
                    Bundle bundle = new Bundle();
                    bundle.putString("idevent", String.valueOf(events.get(spinnerEvent.getSelectedItemPosition()).getIdEvent()));
                    Fragment fragment = new ReadDataFragment();
                    fragment.setArguments(bundle);
                    helper.changeFragment(fragment);
                }
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

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Database");
    }
}

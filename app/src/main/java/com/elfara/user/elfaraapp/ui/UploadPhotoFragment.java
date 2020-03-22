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
public class UploadPhotoFragment extends Fragment {
    private HelperUtils helper;
    private View view;
    private LinearLayout llMain, llDateForm;
    private ProgressBar progressBar;
    private Spinner spinnerEvent;
    private Switch switchDate;
    private EditText edtDateFrom, edtDateTo;
    private Button btnUpload, btnSubmit;
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


    public UploadPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload_photo, container, false);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        helper = new HelperUtils((AppCompatActivity)getActivity(), getContext());
        llMain = view.findViewById(R.id.llMainUploadPhoto);
        llDateForm = view.findViewById(R.id.llDateFormUploadPhoto);
        progressBar = view.findViewById(R.id.progressBarUploadPhoto);
        spinnerEvent = view.findViewById(R.id.spinnerUploadPhoto);
        switchDate = view.findViewById(R.id.switchUploadPhoto);
        edtDateFrom = view.findViewById(R.id.edtDateFromUploadPhoto);
        edtDateTo = view.findViewById(R.id.edtDateToUploadPhoto);
        btnUpload = view.findViewById(R.id.btnUploadUploadPhoto);
        btnSubmit = view.findViewById(R.id.btnSubmitUploadPhoto);

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
                Bundle bundle = new Bundle();
                bundle.putString("idevent", String.valueOf(events.get(spinnerEvent.getSelectedItemPosition()).getIdEvent()));
                bundle.putString("nama_event", events.get(spinnerEvent.getSelectedItemPosition()).getNama());
                if (switchDate.isChecked()) {
                    if (!edtDateFrom.getText().toString().isEmpty() && !edtDateTo.getText().toString().isEmpty()) {
                        bundle.putString("dateFrom", edtDateFrom.getText().toString());
                        bundle.putString("dateTo", edtDateTo.getText().toString());
                        PhotoListFragment photoListFragment = new PhotoListFragment();
                        photoListFragment.setArguments(bundle);
                        helper.changeFragment(photoListFragment);
                    } else {
                        Toast.makeText(getContext(), "Date field cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    PhotoListFragment photoListFragment = new PhotoListFragment();
                    photoListFragment.setArguments(bundle);
                    helper.changeFragment(photoListFragment);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Upload Photo");
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

package com.elfara.user.elfaraapp.ui;


import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Model.ReadData;
import com.elfara.user.elfaraapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DateReadData extends Fragment {
    private View view;
    private EditText edtDateFrom, edtDateTo;
    private Button btnSubmit;

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

        edtDateFrom = view.findViewById(R.id.edtDateFromReadData);
        edtDateTo = view.findViewById(R.id.edtDateToReadData);
        btnSubmit = view.findViewById(R.id.btnSubmitReadData);

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
                new DatePickerDialog(getActivity(), dateSetListenerFrom, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getReadData(edtDateFrom.getText().toString(), edtDateTo.getText().toString());
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
                    System.out.println("DATA GET : " + response.body());
                } else {
                    Toast.makeText(view.getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ReadData>> call, Throwable t) {
                Toast.makeText(view.getContext(), "Failed Response", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

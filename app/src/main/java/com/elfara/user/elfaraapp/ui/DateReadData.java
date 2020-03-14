package com.elfara.user.elfaraapp.ui;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.elfara.user.elfaraapp.Function.FunctionEventLog;
import com.elfara.user.elfaraapp.R;
import com.elfara.user.elfaraapp.Utils.HelperUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DateReadData extends Fragment {
    private View view;
    private HelperUtils helper;
    private EditText edtDateFrom, edtDateTo;
    private Button btnSubmit;
    private FunctionEventLog functionEventLog;

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

        edtDateFrom = view.findViewById(R.id.edtDateFromReadData);
        edtDateTo = view.findViewById(R.id.edtDateToReadData);
        btnSubmit = view.findViewById(R.id.btnSubmitReadData);

        functionEventLog = new FunctionEventLog(view.getContext());

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
                functionEventLog.writeEventLog("Open Database From " + edtDateFrom.getText().toString() + " To " + edtDateTo.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putString("dateFrom", edtDateFrom.getText().toString());
                bundle.putString("dateTo", edtDateTo.getText().toString());
                Fragment fragment = new ReadDataFragment();
                fragment.setArguments(bundle);
                helper.changeFragment(fragment);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Database");
    }
}

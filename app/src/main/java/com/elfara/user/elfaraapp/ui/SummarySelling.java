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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Model.SummarySell;
import com.elfara.user.elfaraapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummarySelling extends Fragment {
    private View view;
    private EditText edtDateFrom, edtDateTo;
    private Button btnSummary;
    private GraphView graphSummarySelling;
    private ProgressBar progressBar;

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

    public SummarySelling() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_summary_selling, container, false);

        edtDateFrom = view.findViewById(R.id.edtDateFromSelling);
        edtDateTo = view.findViewById(R.id.edtDateToSelling);
        btnSummary = view.findViewById(R.id.btnSummarySelling);
        graphSummarySelling = view.findViewById(R.id.graphSummarySelling);
        progressBar = view.findViewById(R.id.progressBarSelling);

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

        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtDateTo.getText().toString().isEmpty() && !edtDateFrom.getText().toString().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    graphSummarySelling.removeAllSeries();
                    SummarySell sum = new SummarySell();
                    sum.setTanggaldari(edtDateFrom.getText().toString());
                    sum.setTanggalsampai(edtDateTo.getText().toString());
                    summary(sum);
                } else {
                    Toast.makeText(view.getContext(), "All Field must be Filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public void summary(SummarySell summarySell) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<SummarySell>> summarysell = apiInterface.getSummarySelling(
                summarySell.getTanggaldari(), summarySell.getTanggalsampai()
        );

        summarysell.enqueue(new Callback<List<SummarySell>>() {
            @Override
            public void onResponse(Call<List<SummarySell>> call, Response<List<SummarySell>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(view.getContext(), "Select Success!!", Toast.LENGTH_SHORT).show();
                    System.out.println("RESPONSE : " + response.body());
                    graphSummarySelling.addSeries(new LineGraphSeries(updateGraph(response.body())));
                    graphSummarySelling.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(view.getContext(), "Select Failed!!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<SummarySell>> call, Throwable t) {
                Toast.makeText(view.getContext(), "No Data Found!!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public DataPoint[] updateGraph(List<SummarySell> summarySells) {
        DataPoint[] dataPoints = new DataPoint[summarySells.size()];
        int count = 0;
        for (SummarySell sell: summarySells) {
            DataPoint dataPoint = new DataPoint(dateToInt(sell.getTanggal()), sell.getSelling());
            dataPoints[count] = dataPoint;
            count++;
        }
        return dataPoints;
    }

    private int dateToInt(String tanggal) {
        String[] array_tanggal = tanggal.split("-");
        return Integer.parseInt(array_tanggal[2]);
    }
}

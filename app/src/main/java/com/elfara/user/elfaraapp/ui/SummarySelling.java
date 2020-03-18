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
import com.elfara.user.elfaraapp.Model.SummarySell;
import com.elfara.user.elfaraapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummarySelling extends Fragment {
    private View view;
    private LinearLayout llMain, llDateForm;
    private Spinner spinnerEvent, spinnerMonth;
    private Switch switchDate;
    private EditText edtDateFrom, edtDateTo;
    private Button btnSummary;
    private GraphView graphSummarySelling;
    private ProgressBar progressBar;
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

    public SummarySelling() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_summary_selling, container, false);

        llMain = view.findViewById(R.id.llInputDateSelling);
        llDateForm = view.findViewById(R.id.llDateFormSelling);
        spinnerEvent = view.findViewById(R.id.spinnerReportSelling);
        spinnerMonth = view.findViewById(R.id.spinnerMonthSelling);
        switchDate = view.findViewById(R.id.switchDateSelling);
        edtDateFrom = view.findViewById(R.id.edtDateFromSelling);
        edtDateTo = view.findViewById(R.id.edtDateToSelling);
        btnSummary = view.findViewById(R.id.btnSummarySelling);
        graphSummarySelling = view.findViewById(R.id.graphSummarySelling);
        progressBar = view.findViewById(R.id.progressBarSelling);

        functionEventLog = new FunctionEventLog(view.getContext());
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        getEventList();

        switchDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    llDateForm.setVisibility(View.VISIBLE);
                    spinnerMonth.setVisibility(View.GONE);
                } else {
                    llDateForm.setVisibility(View.GONE);
                    spinnerMonth.setVisibility(View.VISIBLE);
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

        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchDate.isChecked()) {
                    if (!edtDateTo.getText().toString().isEmpty() && !edtDateFrom.getText().toString().isEmpty()) {
                        String dateTo = edtDateTo.getText().toString();
                        String dateFrom = edtDateFrom.getText().toString();
                        Boolean validate = validateDate(dateTo, dateFrom);
                        if (validate) {
                            progressBar.setVisibility(View.VISIBLE);
                            graphSummarySelling.removeAllSeries();
                            SummarySell sum = new SummarySell();
                            sum.setTanggaldari(edtDateFrom.getText().toString());
                            sum.setTanggalsampai(edtDateTo.getText().toString());
                            summary(sum);
                        }
                    } else {
                        Toast.makeText(view.getContext(), "All Field must be Filled", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    summaryWithoutDate(events.get(spinnerEvent.getSelectedItemPosition()).getIdEvent(), spinnerMonth.getSelectedItemPosition()+1);
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
        getActivity().setTitle("Report Selling");
    }

    public void summary(SummarySell summarySell) {
        Call<List<SummarySell>> summarysell = apiInterface.getSummarySelling(
                summarySell.getTanggaldari(), summarySell.getTanggalsampai()
        );

        summarysell.enqueue(new Callback<List<SummarySell>>() {
            @Override
            public void onResponse(Call<List<SummarySell>> call, Response<List<SummarySell>> response) {
                if (response.isSuccessful()) {
                    functionEventLog.writeEventLog("Open Report Selling From " + edtDateFrom.getText().toString() + " To " + edtDateTo.getText().toString());
                    Toast.makeText(view.getContext(), "Select Success!!", Toast.LENGTH_SHORT).show();
                    graphSummarySelling.addSeries(new LineGraphSeries(updateGraph(response.body())));
                    graphSummarySelling.getViewport().setScalable(true);
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

    private void summaryWithoutDate(int idEvent, int month) {
        Call<List<SummarySell>> summarySell = apiInterface.getSummarySellingByEvent(
                idEvent, month
        );
        summarySell.enqueue(new Callback<List<SummarySell>>() {
            @Override
            public void onResponse(Call<List<SummarySell>> call, Response<List<SummarySell>> response) {
                if (response.isSuccessful()) {
                    functionEventLog.writeEventLog("Open Report Selling From " + edtDateFrom.getText().toString() + " To " + edtDateTo.getText().toString());
                    Toast.makeText(view.getContext(), "Select Success!!", Toast.LENGTH_SHORT).show();
                    graphSummarySelling.addSeries(new LineGraphSeries(updateGraph(response.body())));
                    graphSummarySelling.getViewport().setScalable(true);
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
            System.out.println("DATE : " + dateToInt(sell.getTanggal()));
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

    private Boolean validateDate(String dateTo, String dateFrom) {
        String[] to = dateTo.split("-");
        String[] from = dateFrom.split("-");
        if (!from[0].equals(to[0]) || !from[1].equals(to[1])) {
            Toast.makeText(view.getContext(), "Can only select date in the same month!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(from[2]) > Integer.parseInt(to[2])) {
            Toast.makeText(view.getContext(), "Date from shouldn't bigger than date to!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}

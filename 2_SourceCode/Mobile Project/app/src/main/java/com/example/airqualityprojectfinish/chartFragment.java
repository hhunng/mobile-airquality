package com.example.airqualityprojectfinish;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import java.lang.Thread;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Attr;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class chartFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Button firstDateTime, lastDateTime, graphButton;
    private LineChart lineChart;
    private List<Entry> seriesData = new ArrayList<>();

    AttributeData attributeData;
    AttributeData attributeDatas;
    Long beginTimeStamp, endTimeStamp;
    String selectedFeatures;
    Spinner spinner;
    APIInterface apiInterface;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public chartFragment() {
    }
    public static chartFragment newInstance(String param1, String param2) {
        chartFragment fragment = new chartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        firstDateTime = view.findViewById(R.id.firstDateTime);
        lastDateTime = view.findViewById(R.id.lastDateTime);
        lineChart = view.findViewById(R.id.chart);
        graphButton = view.findViewById(R.id.buttonGraph);

        spinner = view.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.features, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        timeStamp initialDateTime = new timeStamp(8, 11, 2023);
        timeStamp endOfDateTime = getCurrentDateTime();

        timeStamp getFirstDateTime = new timeStamp();
        timeStamp getLastDateTime = new timeStamp();

        firstDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(initialDateTime, getFirstDateTime);
            }
        });
        lastDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(endOfDateTime, getLastDateTime);
            }
        });

        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstDateTime.setText(String.valueOf(getFirstDateTime.day + "/" + getFirstDateTime.month + "/" + getFirstDateTime.year + " " + getFirstDateTime.hour + ":" + getFirstDateTime.minute));
                lastDateTime.setText(String.valueOf(getLastDateTime.day + "/" + getLastDateTime.month + "/" + getLastDateTime.year + " " + getLastDateTime.hour + ":" + getLastDateTime.minute));

                seriesData.clear();
                Call call = apiInterface.getAttributes("5zI6XqkQVSfdgOrZ1MyWEf");
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(new Gson().toJson(response.body()));
                            JSONObject attributes = jsonObject.getJSONObject("attributes");
                            JSONObject rainfall = attributes.getJSONObject("rainfall");
                            JSONObject temperature = attributes.getJSONObject("temperature");
                            JSONObject humidity = attributes.getJSONObject("humidity");
                            JSONObject windSpeed = attributes.getJSONObject("windSpeed");

                            Long temperatureTimeStamp = temperature.getLong("timestamp");
                            Long humidityTimeStamp = humidity.getLong("timestamp");
                            Long windSpeedTimeStamp = windSpeed.getLong("timestamp");
                            Long rainfallTimeStamp = rainfall.getLong("timestamp");

                            timeStamp convertedTemperatureTimeStamp = new timeStamp();
                            timeStamp convertedHumidityTimeStamp = new timeStamp();
                            timeStamp convertedWindSpeedTimeStamp = new timeStamp();
                            timeStamp convertedRainfallTimeStamp = new timeStamp();

                            convertedTemperatureTimeStamp = TimestampConverter(temperatureTimeStamp, convertedTemperatureTimeStamp);
                            convertedHumidityTimeStamp = TimestampConverter(humidityTimeStamp, convertedHumidityTimeStamp);
                            convertedWindSpeedTimeStamp = TimestampConverter(windSpeedTimeStamp, convertedWindSpeedTimeStamp);
                            convertedRainfallTimeStamp = TimestampConverter(rainfallTimeStamp, convertedRainfallTimeStamp);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
                beginTimeStamp = convertTimeStamp(getFirstDateTime.year, getFirstDateTime.month, getFirstDateTime.day, getFirstDateTime.hour, getFirstDateTime.minute);
                endTimeStamp = convertTimeStamp(getLastDateTime.year, getLastDateTime.month, getLastDateTime.day, getLastDateTime.hour, getLastDateTime.minute);
                attributeData = new AttributeData(beginTimeStamp, endTimeStamp, "lttp");
                displayGraphFeatures(selectedFeatures);
            }
        });
        attributeDatas = new AttributeData(1701369665000L, 1701801665000L, "lttp");
        Call callData = apiInterface.postAttributeData("5zI6XqkQVSfdgOrZ1MyWEf", "rainfall", attributeDatas);
        callData.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                int j = 0;
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        for (int i = jsonArray.length() - 1 ; i >= 0; i--) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String xs = jsonObject.getString("x");
                            double value = Double.parseDouble(xs);
                            long x = (long) value;
                            double y = jsonObject.getDouble("y");
                            Log.d("APICALL", String.valueOf(x));
                            timeStamp convertedTimeStamp = new timeStamp();
                            convertedTimeStamp = TimestampConverter(x ,convertedTimeStamp);
                            Log.d("APICALL", String.valueOf(y));
                            DateTimeFormatter formatter = null;
                            String formattedDateTime = "";
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                LocalDateTime dateTime = LocalDateTime.of(convertedTimeStamp.year, convertedTimeStamp.month, convertedTimeStamp.day, convertedTimeStamp.hour, convertedTimeStamp.minute);
                                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                formattedDateTime = dateTime.format(formatter);
                            }
                            Log.d("APICALL", formattedDateTime);
                            seriesData.add(new Entry(j, (float) y));
                            j++;
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    chart("rainfall", seriesData);
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
        return view;
    }
    private void showDatePicker(timeStamp initialDateTime, timeStamp getDateTime) {
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                getDateTime.day = day;
                getDateTime.month = month + 1;
                getDateTime.year = year;
                showTimePicker(getDateTime);
            }
        }, initialDateTime.year, initialDateTime.month, initialDateTime.day);
        dialog.show();
    }
    private void showTimePicker(timeStamp getDateTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                getDateTime.hour = selectedHour;
                getDateTime.minute = selectedMinute;
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private timeStamp TimestampConverter(Long timestamp, timeStamp specificTimeStamp)
    {
        LocalDateTime dateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
            specificTimeStamp.year = dateTime.getYear();
            specificTimeStamp.month = dateTime.getMonthValue();
            specificTimeStamp.day = dateTime.getDayOfMonth();
            specificTimeStamp.hour = dateTime.getHour();
            specificTimeStamp.minute = dateTime.getMinute();
        }
        return specificTimeStamp;
    }
    private long convertTimeStamp(int year, int month, int day, int hour, int minute) {
        long timestamp = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime datetime = LocalDateTime.of(year, month, day, hour, minute);
            ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
            OffsetDateTime offsetDateTime = datetime.atZone(zoneId).toOffsetDateTime();
            timestamp = offsetDateTime.toEpochSecond() * 1000;
            }
        return timestamp;
    }
    private timeStamp getCurrentDateTime ()
    {
        timeStamp getCurrent = new timeStamp();
        int day = 0, month = 0, year = 0 , hour = 0, minute = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
            LocalDateTime now = LocalDateTime.now(vietnamZone);

            day = now.getDayOfMonth();
            month = now.getMonthValue();
            year = now.getYear();
            hour = now.getHour();
            minute = now.getMinute();
        }
        getCurrent = new timeStamp(day, month, year, hour, minute);
        return getCurrent;
    }
    private void chart (String name, List<Entry> seriesData) {
        float maxYValue = 0f;
        for (Entry entry : seriesData) {
            float yValue = entry.getY();
            if (yValue > maxYValue) {
                maxYValue = yValue;
            }
        }
        Description description = new Description();
        description.setText("");
        description.setPosition(150f, 15f);
        lineChart.setDescription(description);
        lineChart.getAxisRight().setDrawLabels(false);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false);
        xAxis.setLabelCount(4);
        xAxis.setGranularity(1f);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(maxYValue + 5f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);

        LineDataSet dataset1 = new LineDataSet(seriesData, name);
        switch (name) {
            case "temperature":
                dataset1.setColor(Color.RED);
                break;
            case "humidity":
                dataset1.setColor(Color.CYAN);
                break;
            case "windSpeed":
                dataset1.setColor(Color.GREEN);
                break;
            case "rainfall":
                dataset1.setColor(Color.BLUE);
                break;
            default:
                break;
        }
        LineData lineData = new LineData(dataset1);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getItemAtPosition(i).toString()) {
            case "Temperature":
                selectedFeatures = "temperature";
                break;
            case "Humidity":
                selectedFeatures = "humidity";
                break;
            case "Wind Speed":
                selectedFeatures = "windSpeed";
                break;
            case "Rainfall":
                selectedFeatures = "rainfall";
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void displayGraphFeatures (String feature)
    {

        Call callData = apiInterface.postAttributeData("5zI6XqkQVSfdgOrZ1MyWEf", feature, attributeData);
        callData.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                int j = 0;
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        for (int i = jsonArray.length() - 1 ; i >= 0; i--) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String xs = jsonObject.getString("x");
                            double value = Double.parseDouble(xs);
                            long x = (long) value;
                            double y = jsonObject.getDouble("y");
                            Log.d("APICALL", String.valueOf(x));
                            timeStamp convertedTimeStamp = new timeStamp();
                            convertedTimeStamp = TimestampConverter(x ,convertedTimeStamp);
                            Log.d("APICALL", String.valueOf(y));
                            DateTimeFormatter formatter = null;
                            String formattedDateTime = "";
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                LocalDateTime dateTime = LocalDateTime.of(convertedTimeStamp.year, convertedTimeStamp.month, convertedTimeStamp.day, convertedTimeStamp.hour, convertedTimeStamp.minute);
                                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                formattedDateTime = dateTime.format(formatter);
                            }
                            Log.d("APICALL", formattedDateTime);
                            seriesData.add(new Entry(j, (float) y));
                            j++;
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    chart(feature, seriesData);
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

}
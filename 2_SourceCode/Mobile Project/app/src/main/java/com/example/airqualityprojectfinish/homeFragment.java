package com.example.airqualityprojectfinish;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    APIInterface apiInterface;

    TextView temperatureText, humidityText, windSpeedText, rainfallText, dateText;
    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        temperatureText = view.findViewById(R.id.textView3);
        rainfallText = view.findViewById(R.id.textView5);
        humidityText = view.findViewById(R.id.textView9);
        windSpeedText = view.findViewById(R.id.textView7);
        dateText = view.findViewById(R.id.textView2);
        // Get the current date and time in UTC+7
        // Get the current date
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
            ZonedDateTime currentDateTime = ZonedDateTime.now(zoneId);
            // Define the desired format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMMM d '|' hh:mm a");
            String formattedDateTime = currentDateTime.format(formatter);
            dateText.setText(String.valueOf(formattedDateTime));
        }
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call call = apiInterface.getAttributes("5zI6XqkQVSfdgOrZ1MyWEf");
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("APICALL", response.code() + "");
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("APICALL", "response : " + new Gson().toJson(response.body()));

                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        JSONObject attributes = jsonObject.getJSONObject("attributes");

                        //rainfall
                        JSONObject rainfall = attributes.getJSONObject("rainfall");
                        MainActivity.rainfallValue = rainfall.getDouble("value");
                        rainfallText.setText(Double.toString(MainActivity.rainfallValue));
                        Log.e("APICALL", Double.toString(MainActivity.rainfallValue));

                        //temperature
                        JSONObject temperature = attributes.getJSONObject("temperature");
                        MainActivity.temperatureValue = temperature.getDouble("value");
                        //startCountAnimation((int) MainActivity.temperatureValue);
                        temperatureText.setText(Double.toString(MainActivity.temperatureValue));
                        Log.e("APICALL", Double.toString(MainActivity.temperatureValue));


                        //humidity
                        JSONObject humidity = attributes.getJSONObject("humidity");
                        MainActivity.humidityValue = humidity.getDouble("value");
                        humidityText.setText(Double.toString(MainActivity.humidityValue));
                        Log.e("APICALL", Double.toString(MainActivity.humidityValue));

                        //windspeed
                        JSONObject winSpeed = attributes.getJSONObject("windSpeed");
                        MainActivity.windSpeedValue = winSpeed.getDouble("value");
                        windSpeedText.setText(Double.toString(MainActivity.windSpeedValue));
                        Log.e("APICALL", Double.toString(MainActivity.windSpeedValue));

                        //location
                        JSONObject locationObject = attributes.getJSONObject("location");
                        JSONArray coordinatesArray = locationObject.getJSONObject("value").getJSONArray("coordinates");
                        MainActivity.latitude = coordinatesArray.getDouble(0);
                        MainActivity.longitude = coordinatesArray.getDouble(1);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("API CALL", t.getMessage().toString());
            }
        });
        return view;
    }
    void startCountAnimation(int maxValue) {
        ValueAnimator animator = ValueAnimator.ofInt(0, maxValue);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                temperatureText.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.start();
    }
}
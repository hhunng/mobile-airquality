package com.example.airqualityprojectfinish;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.w3c.dom.Text;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dbFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dbFragment extends Fragment {


    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    APIInterface apiInterface;
    TextView textView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    public dbFragment() {
        // Required empty public constructor
    }

    public static dbFragment newInstance(String param1, String param2) {
        dbFragment fragment = new dbFragment();
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
        View view = inflater.inflate(R.layout.fragment_db, container, false);
        textView = view.findViewById(R.id.myMap);
        //handle permissions first, before map is created
        String[] permissions = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION};
        requestPermissionsIfNecessary(permissions);

        //load/initialize the osmdroid configuration
        Context ctx = requireActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's
        //tile servers will get you banned based on this string

        //inflate and create the map
        map = view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(MainActivity.longitude, MainActivity.latitude);
        long animationDuration = 2000;
        double ZoomIn = 19.0;
        mapController.setCenter(startPoint);
        mapController.animateTo(startPoint, ZoomIn, animationDuration);

        Marker marker = new Marker(map);
        marker.setPosition(new GeoPoint(MainActivity.longitude, MainActivity.latitude));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        Drawable markerIcon = getResources().getDrawable(R.drawable.secondmarker);
        marker.setIcon(markerIcon);

        marker.setTitle("Longitude: " + String.valueOf(MainActivity.longitude) + "\nLatitude: " + String.valueOf(MainActivity.latitude));
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                marker.showInfoWindow();
                mapView.getController().animateTo(marker.getPosition());
                clickOpenBottomSheetDialog();
                return true;
            }
        });
        map.getOverlays().add(marker);
        map.invalidate();

        return view;
    }

    private void clickOpenBottomSheetDialog() {
        View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(viewDialog);

        TextView temp = viewDialog.findViewById(R.id.temp);
        TextView humid = viewDialog.findViewById(R.id.humid);
        TextView wind = viewDialog.findViewById(R.id.wind);
        TextView rain = viewDialog.findViewById(R.id.rain);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call call = apiInterface.getAttributes("5zI6XqkQVSfdgOrZ1MyWEf");
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        JSONObject attributes = jsonObject.getJSONObject("attributes");

                        //rainfall
                        JSONObject rainfall = attributes.getJSONObject("rainfall");
                        MainActivity.rainfallValue = rainfall.getDouble("value");
                        rain.setText("Rainfall                                                                           " + Double.toString(MainActivity.rainfallValue));
                        Log.e("APICALL", Double.toString(MainActivity.rainfallValue));

                        //temperature
                        JSONObject temperature = attributes.getJSONObject("temperature");
                        MainActivity.temperatureValue = temperature.getDouble("value");
                        //startCountAnimation((int) MainActivity.temperatureValue);
                        temp.setText("Temperature                                                                " + Double.toString(MainActivity.temperatureValue));


                        //humidity
                        JSONObject humidity = attributes.getJSONObject("humidity");
                        MainActivity.humidityValue = humidity.getDouble("value");
                        humid.setText("Humidity                                                                        " + Double.toString(MainActivity.humidityValue));

                        //windspeed
                        JSONObject winSpeed = attributes.getJSONObject("windSpeed");
                        MainActivity.windSpeedValue = winSpeed.getDouble("value");
                        wind.setText("Wind Speed                                                                   " + Double.toString(MainActivity.windSpeedValue));

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
        bottomSheetDialog.show();

        Button btnDetail = viewDialog.findViewById(R.id.buttonShare);
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragmentContainer,new homeFragment());
                fr.commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        //Configuration.getInstance().load(requireContext(), prefs);
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        //Configuration.getInstance().save(requireContext(), prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permissions[i]);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
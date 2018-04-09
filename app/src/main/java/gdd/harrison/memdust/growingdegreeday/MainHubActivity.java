package gdd.harrison.memdust.growingdegreeday;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

public class MainHubActivity extends AppCompatActivity {

    Button mapScreenSwitchButton;
    Button graphScreenSwitchButton;
    Button dataFetchingButton;
    Button settingsButton;
    GDDDataOrganizer organizer = new GDDDataOrganizer();
    TextView latitude;
    TextView longitude;
    String[] dataForGraph = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_screen_experimental);
        setUpIds();
        setLatitudeAndLongitude();
        setUpListeners();
    }

    protected void onStart(){
        super.onStart();
        setLatitudeAndLongitude();
        setUpListeners();
    }

    private double[] checkLocationData() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double[] latlongPair = new double[2];
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return latlongPair;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latlongPair[0] = location.getLongitude();
        latlongPair[1] = location.getLatitude();
        return latlongPair;
    }

    protected void setLatitudeAndLongitude(){
        organizer.setLatitude(latitude.getText().toString());
        organizer.setLongitude(longitude.getText().toString());
    }

    protected void setUpIds(){
        mapScreenSwitchButton = findViewById(R.id.mapButton);
        graphScreenSwitchButton = findViewById(R.id.graphButton);
        dataFetchingButton = findViewById(R.id.getLocation);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        settingsButton=findViewById(R.id.settingsButton);
    }

    protected void setUpListeners(){
        listenForMapButtonClick(mapScreenSwitchButton);
        listenForGraphButtonClick(graphScreenSwitchButton);
        listenForDataFetchingClick(dataFetchingButton);
        listenForSettingsButtonClick();
    }

    protected void listenForSettingsButtonClick(){
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToSettingsActivity();
            }
        });
    }

    protected void listenForMapButtonClick(Button buttonListener){
        buttonListener.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                switchToMapActivity();
            }
        });
    }

    protected void listenForDataFetchingClick(Button buttonListener){
        buttonListener.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                double[] latlon = checkLocationData();
                replacelatlonTextView(latlon);
            }
        });
    }

    protected void switchToSettingsActivity(){
        Intent settingActivitySwitchIntent = new Intent(this, SettingsScreenActivity.class);
        startActivity(settingActivitySwitchIntent);
    }

    protected void replacelatlonTextView(double[] latlong){
        String latitude = Double.toString(latlong[1]);
        String longitude = Double.toString(latlong[0]);
        this.latitude.setText(latitude);
        this.longitude.setText(longitude);
    }

    protected void switchToMapActivity(){
        Intent mapActivitySwitchIntent = new Intent(this, MapScreen.class);
        startActivity(mapActivitySwitchIntent);
    }

    protected void listenForGraphButtonClick(Button buttonListener){
        buttonListener.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                organizer.setLatitude(latitude.getText().toString());
                organizer.setLongitude(longitude.getText().toString());
                String[] data = organizer.beginRetrievingData();
                addOnlySomePartsOfTheData(data);
                dataForGraph[2] = organizer.getBlackLayer();
                dataForGraph[3] = organizer.getSilkLayer();
                dataForGraph[4] = organizer.getAccumulatedAverage();
                switchToGraphActivity();
            }
        });
    }

    protected void addOnlySomePartsOfTheData(String[] data){
        System.arraycopy(data, 0, dataForGraph, 0, 2);
    }

    protected void switchToGraphActivity(){
        Intent graphActivitySwitchIntent = new Intent(this, GraphScreen.class );
        graphActivitySwitchIntent.putExtra("dataStringArray",dataForGraph);
        startActivity(graphActivitySwitchIntent);
    }

}

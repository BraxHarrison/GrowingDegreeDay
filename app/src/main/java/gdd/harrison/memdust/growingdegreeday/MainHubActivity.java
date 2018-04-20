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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainHubActivity extends AppCompatActivity {

    ArrayList<Button> buttonList = new ArrayList<>();
    GDDDataOrganizer organizer = new GDDDataOrganizer();
    String[] dataForGraph = new String[8];
    String[] dataForTable = new String[4];
    int[] buttonIds = new int[]{R.id.mapButton, R.id.graphButton, R.id.tableButton, R.id.settingsButton, R.id.getLocation};
    Class[] classList = new Class[]{MapScreen.class, GraphScreen.class, TableScreen.class, SettingsScreenActivity.class};
    TextView latitude;
    TextView longitude;
    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_screen_experimental);
        setUpButtons();
        setUpLatitudeAndLongitude();
        setLatitudeAndLongitude();
        setUpListeners();
        setUpTextChangedListeners();
        data = organizer.beginRetrievingData();
    }

    protected void setUpTextChangedListeners(){
        longitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setLatitudeAndLongitude();
                data = organizer.beginRetrievingData();
            }
        });
    latitude.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            setLatitudeAndLongitude();
            data = organizer.beginRetrievingData();
        }
    });

    }


    protected void setUpButtons(){
        for (int buttonId : buttonIds) {
            Button thisButton = findViewById(buttonId);
            buttonList.add(thisButton);
        }
    }

    protected void setUpLatitudeAndLongitude(){
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
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
        Location location;
        if (lm != null) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location!= null){
                latlongPair[0] = location.getLongitude();
                latlongPair[1] = location.getLatitude();
            }
            else{
                latlongPair[0] = -85.3864;
                latlongPair[1] = 40.116;
            }
        }
        return latlongPair;
    }

    protected void setLatitudeAndLongitude(){
        organizer.setLatitude(latitude.getText().toString());
        organizer.setLongitude(longitude.getText().toString());
    }

    private void setUpListeners(){
        for (int i = 0; i < buttonList.size(); i++){
            final int finalI = i;
            buttonList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((finalI == 1) || (finalI == 2)){
                        addOnlySomePartsOfTheData(data);
                        addCorrectDataNeeded(finalI);
                        switchToCorrectActivity(finalI, classList[finalI]);
                    }
                    else if ((finalI == 4)){
                        replacelatlonTextView(checkLocationData());
                    }
                    else{
                        switchToCorrectActivity(finalI, classList[finalI]);
                    }
                }
            });
        }
    }

    void switchToCorrectActivity(int i, Class specificClass){
        Intent viewIntent = new Intent(this, specificClass);
        viewIntent = putCorrectExtras(viewIntent, i);
        startActivity(viewIntent);
    }

    Intent putCorrectExtras(Intent intent, int i){
        if (i == 1){
            intent.putExtra("dataForDisplay", dataForGraph);
        }
        else if (i == 2){
            intent.putExtra("dataForDisplay", dataForTable);
        }
        return intent;
    }


    void addCorrectDataNeeded(int i){
        if (i == 1){
            addOnlySomePartsOfTheData(data);
            dataForGraph[2] = organizer.getBlackLayer();
            dataForGraph[3] = organizer.getSilkLayer();
            dataForGraph[4] = organizer.getAccumulatedAverage();
            dataForGraph[5] = organizer.getGDDProjection();
            dataForGraph[6] = String.valueOf(organizer.getCurrentDay());
            dataForGraph[7] = organizer.getFreezeData();
        }
        if (i==2){
            addOnlySomePartsOfTheDataTable(data);
            dataForTable[1] = organizer.getCornStages();
            dataForTable[2] = organizer.getBlackLayer();
            dataForTable[3] = organizer.getSilkLayer();
        }
    }

    protected void replacelatlonTextView(double[] latlong){
        String latitude = Double.toString(latlong[1]);
        String longitude = Double.toString(latlong[0]);
        this.latitude.setText(latitude);
        this.longitude.setText(longitude);
    }

    protected void addOnlySomePartsOfTheData(String[] data){
        System.arraycopy(data, 0, dataForGraph, 0, 2);
    }

    protected void addOnlySomePartsOfTheDataTable(String[] data){
        dataForTable[0] = data[0];
    }

}

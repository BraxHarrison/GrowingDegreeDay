package gdd.harrison.memdust.growingdegreeday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainHubActivity extends AppCompatActivity {

    Button mapScreenSwitchButton;
    Button graphScreenSwitchButton;
    Button dataFetchingButton;
    GDDDataOrganizer organizer = new GDDDataOrganizer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_screen_experimental);
        mapScreenSwitchButton = findViewById(R.id.mapButton);
        graphScreenSwitchButton = findViewById(R.id.graphButton);
        dataFetchingButton = findViewById(R.id.getLocation);
        listenForMapButtonClick(mapScreenSwitchButton);
        listenForGraphButtonClick(graphScreenSwitchButton);
        listenForDataFetchingClick(dataFetchingButton);
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
                String[] data = organizer.beginRetrievingData();
                switchToDataViewingActivity(data);
            }
        });
    }

    protected void switchToMapActivity(){
        Intent mapActivitySwitchIntent = new Intent(this, MapScreen.class);
        startActivity(mapActivitySwitchIntent);
    }
    protected void listenForGraphButtonClick(Button buttonListener){
        buttonListener.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                switchToGraphActivity();
            }
        });

    }
    protected void switchToGraphActivity(){
        Intent graphActivitySwitchIntent = new Intent(this, GraphScreen.class );
        startActivity(graphActivitySwitchIntent);
    }

    protected void switchToDataViewingActivity(String[] data){
        Intent dataViewingActivityIntent = new Intent(this, OnlineDataTestActivity.class);
        dataViewingActivityIntent.putExtra("dataStringArray", data);
        startActivity(dataViewingActivityIntent);
    }
}

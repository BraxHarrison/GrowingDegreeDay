package gdd.harrison.memdust.growingdegreeday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainHubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_screen_experimental);
        Button mapScreenSwitchButton = findViewById(R.id.mapButton);
        Button graphScreenSwitchButton = findViewById(R.id.graphButton);
        listenForMapButtonClick(mapScreenSwitchButton);
        listenForGraphButtonClick(graphScreenSwitchButton);
    }

    protected void listenForMapButtonClick(Button buttonListener){
        buttonListener.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                switchToMapActivity();
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
}

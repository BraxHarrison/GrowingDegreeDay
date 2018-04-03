package gdd.harrison.memdust.growingdegreeday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OnlineDataTestActivity extends AppCompatActivity {

    TextView dataView;
    String[] dataArray;
    Button allDataButton;
    Button currentDataButton;
    Button minDataButton;
    Button forecastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_data_test);
        Intent intent = getIntent();
        dataArray = intent.getStringArrayExtra("dataStringArray");
        dataView = findViewById(R.id.textView);
        allDataButton = findViewById(R.id.AllDataButton);
        listenToAllDataButton();
        minDataButton = findViewById(R.id.CurrentMinButton);
        listenToMinDataButton();
        currentDataButton = findViewById(R.id.CurrentDataButton);
        listenToCurrentDataButton();
        forecastButton = findViewById(R.id.CurrentForecastButton);
        listenToForecastDataButton();
    }

    void listenToAllDataButton(){
        allDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataView.setText(dataArray[2]);
            }
        });
    }

    void listenToCurrentDataButton(){
        currentDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataView.setText(dataArray[0]);
            }
        });
    }

    void listenToMinDataButton(){
        minDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataView.setText(dataArray[1]);
            }
        });
    }

    void listenToForecastDataButton(){
        forecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataView.setText(dataArray[3]);
            }
        });
    }

}

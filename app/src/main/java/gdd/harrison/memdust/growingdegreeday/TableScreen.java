package gdd.harrison.memdust.growingdegreeday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

public class TableScreen extends AppCompatActivity{
    String[] dataArray;
    Spinner smallSpinner;
    Spinner largeSpinner;
    TextView textView;
    String[] cornLayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_screen);
        Intent thisIntent = getIntent();
        dataArray = thisIntent.getStringArrayExtra("dataForTable");
        System.out.println(Arrays.toString(dataArray));
        smallSpinner = (Spinner) findViewById(R.id.spinner);
        largeSpinner = findViewById(R.id.spinner2);
        textView = findViewById(R.id.editText);
        listenForFirstSpinner();
        organizeLayersArray();
    }

    void organizeLayersArray(){
        String[] allLayers = dataArray[1].split(",");
        cornLayers = allLayers;
    }

    void listenForFirstSpinner(){
        smallSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        String currentValues = dataArray[0];
                        String[] currentGDD = currentValues.split(" ");
                        textView.setText(currentGDD[currentGDD.length-1]);
                        break;
                    case 1:
                        textView.setText(cornLayers[0]);
                        break;
                    case 2:
                        textView.setText(cornLayers[1]);
                        break;
                    case 3:
                        textView.setText(cornLayers[2]);
                        break;
                    case 4:
                        textView.setText(cornLayers[3]);
                        break;
                    case 5:
                        textView.setText(cornLayers[4]);
                        break;
                    case 6:
                        textView.setText(dataArray[3]);
                        break;
                    case 7:
                        textView.setText(dataArray[2]);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }
        );
    }

}

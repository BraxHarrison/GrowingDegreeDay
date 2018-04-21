package gdd.harrison.memdust.growingdegreeday;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

public class TableScreen extends AppCompatActivity{
    String[] dataArray;
    Spinner smallSpinner;
    Spinner largeSpinner;
    TextView textView;
    String[] cornLayers;
    LinearLayout changingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_screen);
        Intent thisIntent = getIntent();
        dataArray = thisIntent.getStringArrayExtra("dataForDisplay");
        smallSpinner = (Spinner) findViewById(R.id.spinner);
        largeSpinner = findViewById(R.id.spinner2);
        textView = findViewById(R.id.editText);
        changingList = this.findViewById(R.id.customList);
        generateDateStringsForDisplay();
        generateAccumulatedDataForDisplay();
        generateAverageGDDDataForDisplay();
        listenForFirstSpinner();
        listenForSecondSpinner();
        organizeLayersArray();
    }

    protected void generateAverageGDDDataForDisplay(){
        String[] splitData = dataArray[4].split(", ");
        for (int i = 0; i < splitData.length; i++){
            LinearLayout averageLayout = this.findViewById(R.id.avgList);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(this);
            tv.setLayoutParams(layoutParams);
            tv.setText(splitData[i]);
            averageLayout.addView(tv);
        }
    }

    protected void generateAccumulatedDataForDisplay(){
        String[] splitData = dataArray[5].split(" ");
        if(changingList.getChildCount() > 0){
            changingList.removeAllViews();
        }
        for (int i = 0; i < splitData.length; i++){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(this);
            tv.setLayoutParams(layoutParams);
            tv.setText(splitData[i]);
            changingList.addView(tv);
        }
    }

    @SuppressLint("SetTextI18n")
    void generateDateStringsForDisplay(){
        GDDDataCalculator calendarCalc = new GDDDataCalculator();
        for (int i = 1; i <= 365; i++){
            int[] currentMonthAndDay = calendarCalc.calculateMonthAndDayGivenADay(i);
            LinearLayout dateLayout = this.findViewById(R.id.dateList);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(this);
            tv.setLayoutParams(layoutParams);
            tv.setText(currentMonthAndDay[0] + "/"+currentMonthAndDay[1] + "/" + calendarCalc.getCurrentYear());
            dateLayout.addView(tv);
        }
    }

    void organizeLayersArray(){
        String stringsWithRemovedFrontBrackets = dataArray[1].replace("[", "");
        String stringWithRemovedBackBrackets = stringsWithRemovedFrontBrackets.replace("]", "");
        String[] allLayers = stringWithRemovedBackBrackets.split(",");
        cornLayers = allLayers;
    }

    protected void generateVegetationStagesForDisplay(){
        if(changingList.getChildCount() > 0){
            changingList.removeAllViews();
        }

    }

    void listenForSecondSpinner(){
        largeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        generateAccumulatedDataForDisplay();
                        break;
                    case 1:
                        generateVegetationStagesForDisplay();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });
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

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
    TextView customHeader;

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
        customHeader = findViewById(R.id.customHeader);
        generateDateStringsForDisplay();
        generateAccumulatedDataForDisplay();
        generateAverageGDDDataForDisplay();
        listenForFirstSpinner();
        listenForSecondSpinner();
        organizeLayersArray();
        getCorrectDatesForLayers();
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
        customHeader.setText(R.string.Acc_GDD);
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
        cornLayers = stringWithRemovedBackBrackets.split(",");
    }

    protected void getCorrectDatesForLayers(){
        GDDDataCalculator calendarCalc = new GDDDataCalculator();
        String[] stages = dataArray[6].split(",");
        int[] dateIndices = findFirstIndicesOfTheNewStages(stages);
        for (int i = 0; i < dateIndices.length; i++){
            int[] currentMonthAndDay = calendarCalc.calculateMonthAndDayGivenADay(dateIndices[i]);
            if (i < 5){
                cornLayers[i] = currentMonthAndDay[0] + "/"+currentMonthAndDay[1] + "/" + calendarCalc.getCurrentYear();
            }
            else if(i ==6){
                dataArray[2] = currentMonthAndDay[0] + "/"+currentMonthAndDay[1] + "/" + calendarCalc.getCurrentYear();
            }
            else{
                dataArray[3] = currentMonthAndDay[0] + "/"+currentMonthAndDay[1] + "/" + calendarCalc.getCurrentYear();
            }

        }

    }

    protected void generateVegetationStagesForDisplay(){
        String[] stages = dataArray[6].split(",");
        customHeader.setText(R.string.vegetation_stage);
        if(changingList.getChildCount() > 0){
            changingList.removeAllViews();
        }
        for (int i = 0; i < stages.length; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(this);
            tv.setLayoutParams(layoutParams);
            tv.setText(stages[i]);
            changingList.addView(tv);
        }

    }

    protected int[] findFirstIndicesOfTheNewStages(String[] stages){
        boolean hasBeenV2 = false;
        boolean hasBeenV4 = false;
        boolean hasBeenV6 = false;
        boolean hasBeenV8 = false;
        boolean hasBeenV10 = false;
        boolean hasBeenSilking = false;
        boolean hasBeenBlackLayer = false;
        int[] dateIndices = new int[7];
        for (int i = 0; i < stages.length; i++){
            if ((stages[i].equals(" V2")) && (!hasBeenV2)){
                dateIndices[0] = i;
                hasBeenV2 = true;
            }
            else if((stages[i].equals(" V4")) && (!hasBeenV4)) {
                dateIndices[1] = i;
                hasBeenV4 = true;
            }
            else if((stages[i].equals(" V6")) && (!hasBeenV6)){
                dateIndices[2] = i;
                hasBeenV6 = true;
            }
            else if((stages[i].equals(" V8")) && (!hasBeenV8)){
                dateIndices[3] = i;
                hasBeenV8 = true;
            }
            else if((stages[i].equals(" V10"))&& (!hasBeenV10)){
                dateIndices[4] = i;
                hasBeenV10 = true;
            }
            else if((stages[i].equals(" Silking Layer")) && (!hasBeenSilking)){
                dateIndices[5] = i;
                hasBeenSilking = true;
            }
            else if((stages[i].equals(" Black Layer")) && (!hasBeenBlackLayer)){
                dateIndices[6] = i;
                hasBeenBlackLayer = true;
            }

        }

        return dateIndices;
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

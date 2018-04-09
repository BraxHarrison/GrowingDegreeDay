package gdd.harrison.memdust.growingdegreeday;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class GraphScreen extends AppCompatActivity {

    GraphView graph;
    String[] dataArray;
    ArrayList<LineGraphSeries> allDataSeries = new ArrayList<>();
    Button currentButton;
    Button minTempButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_screen);
        createButtonIds();
        listenToButtons();
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        dataArray = intent.getStringArrayExtra("dataStringArray");
        graph = findViewById(R.id.graph);
        createLineSeries();
    }


    public void createButtonIds(){
        currentButton = findViewById(R.id.CurrentDataButton);
        minTempButton = findViewById(R.id.MinDataButton);
    }

    public void listenToButtons(){
        listenForCurrentButton();
        listenForMinTempButton();
    }

    public void createLineSeries() {
        for (int i = 0; i < 3; i++) {
            if (i == 2){
                LineGraphSeries<DataPoint> horizontalLineSeries = new LineGraphSeries<>(createHorizontalLine(dataArray[i], dataArray[1].length()));
                horizontalLineSeries.setColor(Color.BLACK);
                allDataSeries.add(horizontalLineSeries);
            }
            else {
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(parseData(dataArray[i]));
                series.setTitle("Corn Growing Degree Day Tracker");
                allDataSeries.add(series);
            }
        }
    }

    public DataPoint[] createHorizontalLine(String rawLayerString,int size){
        DataPoint[] dataPoints = new DataPoint[size];
        for (int i = 0; i < size; i++){
            dataPoints[i] = new DataPoint(i, Double.parseDouble(rawLayerString));
        }
        return dataPoints;
    }

    public DataPoint[] parseData(String rawGDDData) {
        String[] splitStrings = rawGDDData.split(" ");
        DataPoint[] dataPoints = new DataPoint[splitStrings.length];
        for (int i = 0; i < splitStrings.length; i++) {
            dataPoints[i] = new DataPoint(i, Integer.parseInt(splitStrings[i]));
            }
        return dataPoints;
    }

    private void listenForCurrentButton(){
        currentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph.removeAllSeries();
                graph.setTitle("Current Growing Degree Data and Projections");
                GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();
                gridLabelRenderer.setHorizontalAxisTitle("Days since January 1st");
                gridLabelRenderer.setVerticalAxisTitle("Growing Degree Days");
                graph.getViewport().setMinX(0);
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxX(365);
                graph.getViewport().setMaxY(3000);
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setXAxisBoundsManual(true);
                graph.addSeries(allDataSeries.get(0));
                graph.addSeries(allDataSeries.get(2));
            }
        });
    }

    private void listenForMinTempButton(){
        minTempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph.removeAllSeries();
                graph.setTitle("Minimum Temperature Measurements");
                GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();
                gridLabelRenderer.setHorizontalAxisTitle("Days since January 1st");
                gridLabelRenderer.setVerticalAxisTitle("Minimum Temperature (°F)");
                graph.getViewport().setMinX(0);
                graph.getViewport().setMinY(-30);
                graph.getViewport().setMaxX(365);
                graph.getViewport().setMaxY(120);
                graph.addSeries(allDataSeries.get(1));
            }
        });
    }

}

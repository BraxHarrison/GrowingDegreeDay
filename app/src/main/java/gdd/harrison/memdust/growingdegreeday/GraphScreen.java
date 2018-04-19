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
        dataArray = intent.getStringArrayExtra("dataForDisplay");
        graph = findViewById(R.id.graph);
        createLineSeries();
        buildGraph();
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
        for (int i = 0; i < 6; i++) {
            if ((i == 2) || (i == 3)){
                LineGraphSeries<DataPoint> horizontalLineSeries = new LineGraphSeries<>(createHorizontalLine(dataArray[i]));
                if (i == 2){
                    horizontalLineSeries.setColor(Color.BLACK);
                }
                if (i == 3){
                    horizontalLineSeries.setColor(Color.RED);
                }
                allDataSeries.add(horizontalLineSeries);
            }
            else if (i == 4){
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(parseAverages(dataArray[i]));
                series.setTitle("Corn GDD Average");
                allDataSeries.add(series);
                series.setColor(Color.rgb(160,32,240));
            }
            else if (i ==5){
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(parseProjection(dataArray[i]));
                series.setTitle("GDD projection");
                allDataSeries.add(series);
                series.setColor(Color.rgb(0, 100, 0));
            }
            else {
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(parseData(dataArray[i]));
                series.setTitle("Corn Growing Degree Day Tracker");
                allDataSeries.add(series);
            }
        }
    }

    public DataPoint[] parseProjection(String rawProjectionString){
        String[] splitStrings = rawProjectionString.split(",");
        DataPoint[] dataPoints = new DataPoint[splitStrings.length];
        double dataPointValue = Double.parseDouble(dataArray[6]);
        for (int i = 0; i < splitStrings.length; i++){
            dataPoints[i] = new DataPoint(dataPointValue, Double.parseDouble(splitStrings[i]));
            dataPointValue++;
        }
        return dataPoints;
    }

    public DataPoint[] parseAverages(String rawAveragesString){
        String[] splitStrings = rawAveragesString.split(",");
        DataPoint[] dataPoints = new DataPoint[splitStrings.length];
        for (int i = 0; i < splitStrings.length; i++){
            dataPoints[i] = new DataPoint(i, Double.parseDouble(splitStrings[i]));
        }
        return dataPoints;
    }

    public DataPoint[] createHorizontalLine(String rawLayerString){
        DataPoint[] dataPoints = new DataPoint[365];
        for (int i = 0; i < 365; i++){
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
                buildGraph();
            }
        });

    }

    private void listenForMinTempButton(){
        minTempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildMinGraph();
            }
        });
    }

    protected void buildMinGraph(){
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

    protected void buildGraph(){
        graph.removeAllSeries();
        graph.setTitle("Current Growing Degree Data and Projections");
        GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();
        gridLabelRenderer.setHorizontalAxisTitle("Days since January 1st");
        gridLabelRenderer.setVerticalAxisTitle("Growing Degree Days");
        graph.getViewport().setMinX(0);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxX(380);
        graph.getViewport().setMaxY(4000);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.addSeries(allDataSeries.get(0));
        graph.addSeries(allDataSeries.get(2));
        graph.addSeries(allDataSeries.get(3));
        graph.addSeries(allDataSeries.get(4));
        graph.addSeries(allDataSeries.get(5));
    }
}

package gdd.harrison.memdust.growingdegreeday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class GraphScreen extends AppCompatActivity {

    GraphView graph;
    String[] dataArray;
    ArrayList<LineGraphSeries> allDataSeries = new ArrayList<>();
    Button currentButton;
    Button minTempButton;
    Button allDataButton;
    Button currentForecastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_screen);
        Intent intent = getIntent();
        dataArray = intent.getStringArrayExtra("dataStringArray");
        graph = findViewById(R.id.graph);
        currentButton = findViewById(R.id.CurrentDataButton);
        listenForCurrentButton();
        minTempButton = findViewById(R.id.MinDataButton);
        listenForMinTempButton();
        allDataButton = findViewById(R.id.AllDataButton);
        listenForAllDataButton();
        currentForecastButton = findViewById(R.id.ForecastDataButton);
        listenForForecastButton();
        createLineSeries();
    }

    public void createLineSeries() {
        for (int i = 0; i < 4; i++) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(parseData(dataArray[i]));
            allDataSeries.add(series);
        }
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
                graph.addSeries(allDataSeries.get(0));
            }
        });
    }

    private void listenForMinTempButton(){
        minTempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph.removeAllSeries();
                graph.addSeries(allDataSeries.get(1));
            }
        });
    }

    private void listenForAllDataButton(){
        allDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph.removeAllSeries();
                graph.addSeries(allDataSeries.get(2));
            }
        });
    }

    private void listenForForecastButton(){
        currentForecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph.removeAllSeries();
                graph.addSeries(allDataSeries.get(3));
            }
        });
    }
}

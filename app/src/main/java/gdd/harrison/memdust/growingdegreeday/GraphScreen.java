package gdd.harrison.memdust.growingdegreeday;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GraphScreen extends AppCompatActivity {

    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_screen);
        graph = findViewById(R.id.graph);
        String dummyInfo = "12,1,121,124,23,2,4,11,93,3";

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(parseData(dummyInfo));
        graph.addSeries(series);
    }
    public DataPoint[] parseData(String rawGDDData){
        String[] splitStrings = rawGDDData.split(",");
        DataPoint[] dataPoints = new DataPoint[splitStrings.length];
        for(int i = 0; i<splitStrings.length;i++){
            dataPoints[i] = new DataPoint(i,Integer.parseInt(splitStrings[i]));
        }
        return dataPoints;
    }
}

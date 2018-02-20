package gdd.harrison.memdust.growingdegreeday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OnlineDataTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_data_test);
        GDDDataParser parser = new GDDDataParser();
        String dataToBeViewed = attemptToRetrieveDataInBackground(parser);
        TextView dataView = findViewById(R.id.textView);
        dataView.setText(dataToBeViewed);
    }

    protected String attemptToRetrieveDataInBackground(GDDDataParser parser){
        String result = "";
        String currentGDDData = "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetAllData=1&lat=40.116&long=-88.182";
        try{
            result = parser.execute(currentGDDData).get();
        }
        catch(Exception e){
            System.out.println("There was an exception.");
        }
        return result;
    }






}

package gdd.harrison.memdust.growingdegreeday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OnlineDataTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_data_test);
        String currentGDDData = "http://mrcc.isws.illinois.edu/U2U/gdd/controllers/datarequest.php?callgetAllData=1&lat=40.116&long=-88.182";
        String result;
        GDDDataParser parser = new GDDDataParser();

        try{
            result = parser.execute(currentGDDData).get();
            TextView dataView = findViewById(R.id.textView);
            dataView.setText(result);
        }
        catch(Exception e){
            System.out.println("There was an exception.");
        }


    }




}

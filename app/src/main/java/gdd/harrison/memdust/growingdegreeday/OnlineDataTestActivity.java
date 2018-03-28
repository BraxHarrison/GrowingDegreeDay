package gdd.harrison.memdust.growingdegreeday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OnlineDataTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_data_test);
        GDDDataOrganizer organizer = new GDDDataOrganizer();
        String dataToBeViewed = organizer.beginRetrievingData();
        TextView dataView = findViewById(R.id.textView);
        dataView.setText(dataToBeViewed);
    }







}

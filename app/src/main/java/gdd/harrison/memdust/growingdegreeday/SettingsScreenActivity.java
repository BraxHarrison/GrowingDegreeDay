package gdd.harrison.memdust.growingdegreeday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import java.lang.reflect.Array;
import java.util.Calendar;

public class SettingsScreenActivity extends AppCompatActivity {

    Intent frostCheckerIntent;
    PendingIntent frostCheckerPending;
    Context context;
    NotificationAlarm freezeChecker;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_settings_screen);
        setUpMonthArraySpinner();
        setUpNotificationsToggle();
        setUpCornMaturityDaysSpinner();
        setUpDayOfMonthSpinner();
        setUpDayFreezingTemperatureSpinner();
    }

    private void setUpNotificationsToggle() {
        Switch toggle = (Switch) findViewById(R.id.receiveNotificationsButton);
        CompoundButton.OnCheckedChangeListener toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setUpAlarm();
//                    frostCheckerIntent = new Intent(context,FreezeCheckerService.class);
//                    startService(frostCheckerIntent);
                }
                else{
                    freezeChecker.stopAlarm(context);
                }
            }
        };
        toggle.setOnCheckedChangeListener(toggleListener);
    }

    private void setUpAlarm() {
        freezeChecker = new NotificationAlarm();
        freezeChecker.setAlarm(context);
    }

    protected void setUpMonthArraySpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.monthSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    protected void setUpCornMaturityDaysSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.cornMaturityDaysSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.corn_maturity_days_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    protected void setUpDayOfMonthSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.dayOfMonthSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.daysOfMonth, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    protected void setUpDayFreezingTemperatureSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.freezing_temperature_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.freezingTemps, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

}


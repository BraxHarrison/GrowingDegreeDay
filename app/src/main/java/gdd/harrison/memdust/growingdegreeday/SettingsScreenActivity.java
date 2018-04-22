package gdd.harrison.memdust.growingdegreeday;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SettingsScreenActivity extends AppCompatActivity {

    Intent frostCheckerIntent;
    PendingIntent frostCheckerPending;
    Context context;
    NotificationAlarm freezeChecker;
    SharedPreferences savedPrefs;
    String[] preferences = new String[3];
    Spinner cornMatSpin;
    Spinner daySpin;
    Spinner monthSpin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getPrefs();
        setContentView(R.layout.activity_settings_screen);
        setUpMonthArraySpinner();
        setUpNotificationsToggle();
        setUpCornMaturityDaysSpinner();
        setUpDayOfMonthSpinner();
        setUpDayFreezingTemperatureSpinner();
        setUpSettingsButton();
        loadSettings();
        cornMatSpin = (Spinner) findViewById(R.id.cornMaturityDaysSpinner);
        daySpin = (Spinner) findViewById(R.id.dayOfMonthSpinner);
        monthSpin = (Spinner) findViewById(R.id.monthSpinner);
    }

    protected void getPrefs(){
        savedPrefs = getSharedPreferences("gdd.PREFS",0);
    }

    private void setUpNotificationsToggle() {
        Switch toggle = (Switch) findViewById(R.id.receiveNotificationsButton);
        CompoundButton.OnCheckedChangeListener toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setUpAlarm();
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

    protected void setUpSettingsButton(){
        Button button = (Button) findViewById(R.id.saveSettingsButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setPreferencesArray();
                saveSettings();
                sendResultToPrevious();
            }
        });
    }

    protected void setPreferencesArray(){
        preferences[0] = cornMatSpin.getSelectedItem().toString();
        preferences[1] = monthSpin.getSelectedItem().toString();
        preferences[2] = daySpin.getSelectedItem().toString();

    }

    protected void sendResultToPrevious(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("resultingArray", preferences);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    protected void saveSettings(){
        SharedPreferences.Editor prefEdit = savedPrefs.edit();


        Spinner freezeSpin = (Spinner) findViewById(R.id.freezing_temperature_spinner);
        Switch notificationButton = (Switch) findViewById(R.id.receiveNotificationsButton);


        prefEdit.putInt("monthSpinner",monthSpin.getSelectedItemPosition());
        prefEdit.putString("monthSpinnerVal",monthSpin.getSelectedItem().toString());
        prefEdit.putInt("cornMaturityDaysSpinner",cornMatSpin.getSelectedItemPosition());
        prefEdit.putInt("cornMaturityDaysSpinnerVal",Integer.parseInt(cornMatSpin.getSelectedItem().toString()));
        prefEdit.putInt("dayOfMonthSpinner",daySpin.getSelectedItemPosition());
        prefEdit.putInt("dayOfMonthSpinner",Integer.parseInt(daySpin.getSelectedItem().toString()));
        prefEdit.putInt("freezing_temperature_spinner",freezeSpin.getSelectedItemPosition());
        prefEdit.putInt("freezing_temperature_spinnerVal",Integer.parseInt(freezeSpin.getSelectedItem().toString()));
        prefEdit.putBoolean("receiveNotificationsButton",notificationButton.isChecked());
        prefEdit.apply();

        Log.d("CREATION",monthSpin.getSelectedItem().toString());
    }

    protected void loadSettings(){

        Spinner monthSpin = (Spinner) findViewById(R.id.monthSpinner);
        Spinner cornMatSpin = (Spinner) findViewById(R.id.cornMaturityDaysSpinner);
        Spinner daySpin = (Spinner) findViewById(R.id.dayOfMonthSpinner);
        Spinner freezeSpin = (Spinner) findViewById(R.id.freezing_temperature_spinner);
        Switch notificationButton = (Switch) findViewById(R.id.receiveNotificationsButton);

        monthSpin.setSelection(savedPrefs.getInt("monthSpinner",0));
        cornMatSpin.setSelection(savedPrefs.getInt("cornMaturityDaysSpinner",0));
        daySpin.setSelection(savedPrefs.getInt("dayOfMonthSpinner",0));
        freezeSpin.setSelection(savedPrefs.getInt("freezing_temperature_spinner",0));
        notificationButton.setChecked(savedPrefs.getBoolean("receiveNotificationsButton",false));


    }

}


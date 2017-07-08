package com.liarstudio.courierservice.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.R;

public class SettingsActivity extends AppCompatActivity {

    RadioGroup radioGroupWeight;
    RadioGroup radioGroupVol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        radioGroupWeight = (RadioGroup) findViewById(R.id.ragioGroupWeight);
        radioGroupVol = (RadioGroup) findViewById(R.id.radioGroupVol);

        if (Package.WEIGHT_PROGRAM_STATE != 0)
            radioGroupWeight.check(R.id.radioButtonLB);

        if (Package.SIZE_PROGRAM_STATE != 0)
            radioGroupVol.check(R.id.radioButtonInch);

        radioGroupWeight.setOnCheckedChangeListener( (group, checkedId) -> {
            SharedPreferences.Editor ed = getSharedPreferences(MainActivity.PREFERENCES_FILENAME,
                    MODE_PRIVATE).edit();
            Package.WEIGHT_PROGRAM_STATE = checkedId == R.id.radioButtonKG ? 0 : 1;
            ed.putInt(MainActivity.WEIGHT_COEFFICIENT, Package.WEIGHT_PROGRAM_STATE);
            ed.commit();


        });
        radioGroupVol.setOnCheckedChangeListener( (group, checkedId) -> {
            SharedPreferences.Editor ed = getSharedPreferences(MainActivity.PREFERENCES_FILENAME,
                    MODE_PRIVATE).edit();
            Package.SIZE_PROGRAM_STATE = checkedId == R.id.radioButtonSM ? 0 : 1;
            ed.putInt(MainActivity.VOL_COEFFICIENT, Package.SIZE_PROGRAM_STATE);
            ed.commit();
        });

    }


}

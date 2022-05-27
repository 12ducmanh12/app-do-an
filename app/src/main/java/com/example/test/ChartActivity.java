package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ChartActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent home = new Intent(ChartActivity.this, MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.navigation_chart:
                        break;
                    case R.id.navigation_alarm:
                        Intent alarm = new Intent(ChartActivity.this,AlarmActivity.class);
                        startActivity(alarm);
                        break;
                    case R.id.navigation_setting:
                        Intent setting = new Intent(ChartActivity.this,SettingActivity.class);
                        startActivity(setting);
                        break;
                }
                return false;
            }
        });
    }
}
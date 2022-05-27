package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SettingActivity extends AppCompatActivity {

    private static Device user1 = new Device("kaitoud","aio_seUr87ZhGBlQcox7zdajzMlXNrgm","Thiết bị 1");
    private static Device user2 = new Device("","","Thiết bị 2");
    private static Device [] users = new Device[]{user1,user2};
    private ActionBar toolbar;
    private NavigationBarView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ArrayAdapter<Device> arrayAdapter
                = new ArrayAdapter<Device>(this, android.R.layout.simple_list_item_1 , users);
        ListView listView = findViewById(R.id.list_device);
        listView.setAdapter(arrayAdapter);

        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent intent2 = new Intent(SettingActivity.this, MainActivity.class);
                        startActivity(intent2);
                    case R.id.navigation_chart:
                        Intent intent = new Intent(SettingActivity.this, ChartActivity.class);
                        startActivity(intent);
                        break;
//                    case R.id.history:
//                        Intent intent1 = new Intent(MainActivity.this, HistoryActivity.class);
//                        startActivity(intent1);
//                        break;
                    case R.id.navigation_setting:
                        break;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Device device = (Device) o;
                Toast.makeText(SettingActivity.this, "Bạn đã chọn:" + " " + device.getDisplayname(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
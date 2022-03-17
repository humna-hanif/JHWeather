package edu.quinnipiac.ser210.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private String location, text;
    private Spinner s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("J&H Weather App");

        String[] arraySpinner = new String[] {
                "NYC", "Los Angeles", "Washington D.C", "Miami", "Tokyo", "Hamden", "London", "Dubai", "Rome", "Sydney", "Barcelona", "Munbai", "Athens", "Paris", "Machu Picchu", "Cairo", "San Francisco", "Austin", "Cape Town", "Istanbul"
        };
        s = (Spinner) findViewById(R.id.locationlist);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }
    public void onClick(View view){
        Intent intent2 = new Intent(MainActivity.this, WeatherInfo.class);
        text = s.getSelectedItem().toString();
        intent2.putExtra("mySpinnerValue", text);
        startActivity(intent2);
    }

}

package edu.quinnipiac.ser210.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    boolean userSelect = false;
    private String location, text;
    private Spinner s;
    private ShareActionProvider provider;
    private View mConstraintLayout;
    private String color = "white";

    //creates share option for action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        provider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.share));
        return super.onCreateOptionsMenu(menu);
    }


    //creates option for action bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            //changes background color when settings is clicked on
            case R.id.settings:
                if (color == "white") {
                    mConstraintLayout.setBackgroundColor(Color.YELLOW);
                    color = "yellow";
                } else if (color == "yellow") {
                    mConstraintLayout.setBackgroundColor(Color.RED);
                    color = "red";
                } else if (color == "red") {
                    mConstraintLayout.setBackgroundColor(Color.BLUE);
                    color = "blue";
                } else {
                    mConstraintLayout.setBackgroundColor(Color.WHITE);
                    color = "white";
                }
                break;
            //goes to next activity when help is clicked on
            case R.id.help:
                Toast.makeText(this, "Help", Toast.LENGTH_LONG).show();
                Intent intent3 = new Intent(MainActivity.this, Help.class);
                startActivity(intent3);

                //share option on action bar
            case R.id.share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "This is a message for you.");
                provider.setShareIntent(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConstraintLayout = findViewById(R.id.layout);

        //creates cities in spinner
        String[] arraySpinner = new String[]{
                "NYC", "Los Angeles", "Washington D.C", "Miami", "Tokyo", "Hamden", "London", "Dubai", "Rome", "Sydney", "Barcelona", "Mumbai", "Athens", "Paris", "Machu Picchu", "Cairo", "San Francisco", "Austin", "Cape Town", "Istanbul"
        };
        s = (Spinner) findViewById(R.id.locationlist);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

    }

    //interaction for action bar
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userSelect = true;
    }


    //on click method for search button
    public void onClick(View view) {
        Intent intent2 = new Intent(MainActivity.this, WeatherInfo.class);
        text = s.getSelectedItem().toString();
        intent2.putExtra("mySpinnerValue", text);
        startActivity(intent2);
    }

}

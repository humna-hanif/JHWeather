package edu.quinnipiac.ser210.weatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherInfo extends AppCompatActivity {
    private static final String LOG_TAG = " ";
    private MainActivity mainActivity;
    private String url1 = "https://foreca-weather.p.rapidapi.com/current/";
    private ShareActionProvider provider;
    private View mConstraintLayout;
    private String color = "white";

    //creates options for action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        provider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.share));
        return super.onCreateOptionsMenu(menu);
    }

    ////creates option for action bar
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
                Toast.makeText(this,"Help", Toast.LENGTH_LONG).show();
                Intent intent3 = new Intent(WeatherInfo.this, Help.class);
                startActivity(intent3);
                //share option on action bar
            case R.id.share:
                Intent intent = new Intent (Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "This is a message for you.");
                provider.setShareIntent(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    //on create method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        mConstraintLayout = findViewById(R.id.layout);
        Bundle bundle = getIntent().getExtras();
        String text = bundle.getString("mySpinnerValue");
        TextView location = (TextView) findViewById(R.id.loc_txt);
        location.setText(text);
        //gets the information for JSON objects from RapidAPI
        new FetchWeatherInfo(){
            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(String result) {
                System.out.println(result);
                try {
                    JSONObject information = new JSONObject(result).getJSONObject("current");

                    String symbol = information.getString("symbolPhrase");
                    TextView sym = (TextView) findViewById(R.id.symbolPhrase);
                    sym.setText(symbol);

                    //changes image depending on weather
                    ImageView weatherImage = (ImageView) findViewById(R.id.weatherImg);
                    if (sym.getText() == "clear" || sym.getText() == "mostly clear") {
                        weatherImage.setImageResource(R.drawable.clear);
                    } else if (sym.getText() == "party cloudy" || sym.getText() == "cloudy") {
                        weatherImage.setImageResource(R.drawable.partlycloudy);
                    } else if (sym.getText() == "light rain") {
                        weatherImage.setImageResource(R.drawable.lightrain);
                    } else if (sym.getText() == "overcast") {
                        weatherImage.setImageResource(R.drawable.overcast);
                    }

                    //time, feels like, temperature, wind speed, and uv for weather in specific city
                    String time = information.getString("time");
                    TextView time1 = (TextView) findViewById(R.id.time);
                    time1.setText("Time: " + time);

                    int feels = information.getInt("feelsLikeTemp");
                    TextView feelLike = (TextView) findViewById(R.id.feelsLike);
                    feelLike.setText("Feels Like: " + feels + " Celsius");

                    int temperature = information.getInt("temperature");
                    TextView temp = (TextView) findViewById(R.id.temperature);
                    temp.setText("Temperature: " + temperature + " Celsius");

                    int windSpd = information.getInt("windSpeed");
                    TextView wind = (TextView) findViewById(R.id.windSpeed);
                    wind.setText("Wind Speed: " + windSpd + " mph");

                    int uv = information.getInt("uvIndex");
                    TextView uvIndx = (TextView) findViewById(R.id.uvIndex);
                    uvIndx.setText("UV Index: " + uv);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(text);
    }
    //connects JSON objects
    class FetchWeatherInfo extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String loc = null;
            String weather = null;
            try{
                URL url = new URL("https://foreca-weather.p.rapidapi.com/location/search/" + strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-RapidAPI-Key","59bf0f0383msh906ced7bb13c086p19f12djsn5cef8d0821e6");
                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                if(in == null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(in));
                JSONObject object = new JSONObject(getStringFromBuffer(reader));
                System.out.println(object.toString());
                JSONArray locations = object.getJSONArray("locations");
                JSONObject locationObject = locations.getJSONObject(0);
                loc = locationObject.getInt("id") + "";

                url = new URL(url1 + loc);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-RapidAPI-Key","59bf0f0383msh906ced7bb13c086p19f12djsn5cef8d0821e6");
                urlConnection.connect();

                in = urlConnection.getInputStream();
                if(in == null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(in));
                weather = getStringFromBuffer(reader);

            }catch(Exception e){
                Log.e(LOG_TAG,"Error" + e.getMessage());
                return null;
            }finally{
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader != null){
                    try{
                        reader.close();
                    }catch (IOException e){
                        Log.e(LOG_TAG,"Error" + e.getMessage());
                        return null;
                    }
                }
            }
            return weather;
        }


    }

    private String getStringFromBuffer(BufferedReader bufferedReader) {
        StringBuffer buffer = new StringBuffer();
        String line;

        if (bufferedReader != null) {
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line + '\n');
                }
                bufferedReader.close();
                return buffer.toString();
            } catch (Exception e) {
                Log.e("MainActivity", "Error" + e.getMessage());
                return null;
            } finally {

            }
        }
        return null;
    }

}

package edu.quinnipiac.ser210.weatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setTitle("J&H Weather App");
        Bundle bundle = getIntent().getExtras();
        String text = bundle.getString("mySpinnerValue");
        TextView location = (TextView) findViewById(R.id.loc_txt);
        location.setText(text);
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

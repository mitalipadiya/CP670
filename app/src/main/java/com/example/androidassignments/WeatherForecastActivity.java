package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecastActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ImageView imageViewWeather;
    private TextView textViewCurrentTemp;
    private TextView textViewMinTemp;
    private TextView textViewMaxTemp;
    private Spinner citySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = findViewById(R.id.progressBar);
        textViewCurrentTemp = findViewById(R.id.textViewCurrentTemp);
        textViewMinTemp = findViewById(R.id.textViewMinTemp);
        textViewMaxTemp = findViewById(R.id.textViewMaxTemp);
        imageViewWeather = findViewById(R.id.imageViewWeather);
        citySpinner = findViewById(R.id.citySpinner);
        setupCitySpinner();

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedCity = (String) adapterView.getItemAtPosition(position);
                String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + selectedCity + ",ca&APPID=20c8110a76914662f305499e68c47632&mode=xml&units=metric";
                progressBar.setVisibility(View.VISIBLE);
                ForecastQuery forecastQuery = new ForecastQuery();
                forecastQuery.execute(apiUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
    }
    private void setupCitySpinner() {
        // Add Canadian cities to the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.canadian_cities,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        private String minTemperature;
        private String maxTemperature;
        private String currentTemperature;
        private Bitmap weatherBitmap;
        private String weatherIcon;
        @Override
        protected String doInBackground(String... params) {
            String apiUrl = params[0];
            try {

                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query.
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                parse(inputStream);

                inputStream.close();
                conn.disconnect();

            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return "Data fetched";
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(progress);
        }
        public void parse(InputStream in) throws XmlPullParserException, IOException {
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();
                    if ("temperature".equals(name)) {
                        currentTemperature = parser.getAttributeValue(null, "value");
                        publishProgress(25);
                        minTemperature = parser.getAttributeValue(null, "min");
                        publishProgress(50);
                        maxTemperature = parser.getAttributeValue(null, "max");
                        publishProgress(75);
                    } else if("weather".equals(name)) {
                        weatherIcon = parser.getAttributeValue(null, "icon");
                        downloadWeatherIcon(weatherIcon);
                    }
                }
            } finally {
                in.close();
            }
        }
        @Override
        protected void onPostExecute(String result) {
            // Update UI with the fetched data
            textViewMinTemp.setText("Min Temp: " + minTemperature);
            textViewMaxTemp.setText("Max Temp: " + maxTemperature);
            textViewCurrentTemp.setText("Current Temp: " + currentTemperature);
            imageViewWeather.setImageBitmap(weatherBitmap);

            // Hide the ProgressBar after data is loaded
            progressBar.setVisibility(View.INVISIBLE);
        }

        private void downloadWeatherIcon(String iconName) {
            try {
                if (fileExists(iconName + ".png")) {
                    FileInputStream fis = openFileInput(iconName + ".png");
                    weatherBitmap = BitmapFactory.decodeStream(fis);
                    fis.close();
                    Log.i("WeatherForecast", "Image found locally: " + iconName + ".png");
                } else {
                    String imageUrl = "https://openweathermap.org/img/w/" + iconName + ".png";
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    weatherBitmap = BitmapFactory.decodeStream(input);
                    input.close();

                    FileOutputStream outputStream = openFileOutput(iconName + ".png", MODE_PRIVATE);
                    weatherBitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    Log.i("WeatherForecast", "Image downloaded: " + iconName + ".png");
                }

                publishProgress(100);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean fileExists(String filename) {
            File file = getBaseContext().getFileStreamPath(filename);
            return file.exists();
        }
    }
    }
package ru.gordeev.httpexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView ouput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ouput = findViewById(R.id.output);
    }

    public void cleanUp(View view) {
        ouput.setText("");
    }

    public void getCurrencies(View view) {
        HttpThread thread = new HttpThread();
        thread.execute();
    }

    private class HttpThread extends AsyncTask<Void, Void, Currencies> {

        @Override
        protected void onPostExecute(Currencies result) {
            String text = result == null ? "Error" : result.toString();

            ouput.setText(text);
        }

        @Override
        protected Currencies doInBackground(Void... voids) {

            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.exchangeratesapi.io/latest")
                    .get()
                    .build();

            Currencies result;

            try (Response response = httpClient.newCall(request).execute()) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                result = gsonBuilder.create().fromJson(response.body().string(), Currencies.class);

            } catch (IOException e) {
                Log.e("ERROR", "Request wasn't send", e);
                result = null;
            }

            return result;
        }
    }

    public class Currencies {
        private String base;
        private Date date;
        private Map<String, Double> rates;

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Map<String, Double> getRates() {
            return rates;
        }

        public void setRates(Map<String, Double> rates) {
            this.rates = rates;
        }

        @Override
        public String toString() {
            return "Currencies{" +
                    "base='" + base + '\'' +
                    ", date=" + date +
                    ", rates=" + rates +
                    '}';
        }
    }

}

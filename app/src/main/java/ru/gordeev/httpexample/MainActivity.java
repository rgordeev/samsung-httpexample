package ru.gordeev.httpexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.IOException;

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

    private class HttpThread extends AsyncTask<Void, Void, JsonElement> {

        @Override
        protected void onPostExecute(JsonElement result) {
            String text = result == null ? "Error" : result.toString();

            ouput.setText(text);
        }

        @Override
        protected JsonElement doInBackground(Void... voids) {

            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.exchangeratesapi.io/latest")
                    .get()
                    .build();

            JsonElement result;

            try (Response response = httpClient.newCall(request).execute()) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                result = gsonBuilder.create().fromJson(response.body().string(), JsonElement.class);

            } catch (IOException e) {
                Log.e("ERROR", "Request wasn't send", e);
                result = null;
            }

            return result;
        }
    }

}

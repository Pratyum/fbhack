package com.example.prjagannath.castus.src;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.prjagannath.castus.API.APICall;
import com.example.prjagannath.castus.CustomEnum.API;
import com.example.prjagannath.castus.R;

import org.json.JSONObject;

public class PostLoginActivity extends AppCompatActivity {

    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2VideoFragment.newInstance())
                    .commit();
            new HealthCheckTask().execute();
        }
    }



    class HealthCheckTask extends AsyncTask<Void, Void, String> {

        APICall apiCall = new APICall(getBaseContext());

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (apiCall.isRequestSuccess(s, true)) {
                Log.d(getClass().getSimpleName(), "onPostExecute: ");
                apiCall.logDebug("String is " + s);
                JSONObject json_answer = null;


            }
        }

        @Override
        protected String doInBackground(Void... params) {

            return apiCall.request(API.GET,"healthCheck", null, null);
        }
    }
}

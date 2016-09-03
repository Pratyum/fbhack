package com.example.prjagannath.castus.src;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prjagannath.castus.API.APICall;
import com.example.prjagannath.castus.CustomEnum.API;
import com.example.prjagannath.castus.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostLoginActivity extends AppCompatActivity {

    private TextView info;
    private String query, requestee_query;
    private String stream_url , secure_stream_url, videoId, fb_id, access_token;
    private Context ctx = this;
    Firebase firebaseDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        firebaseDb = new Firebase("https://castus-5d435.firebaseio.com/");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(getIntent().getIntExtra("notificationID", 0));
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2VideoFragment.newInstance())
                    .commit();
        }

        String friends = getIntent().getStringExtra("friends");
        JSONObject json_friends;
        ArrayList<Pair<String,String>> friends_list_array = new ArrayList<>();
        try {
             json_friends = new JSONObject(friends);
            JSONArray friends_list = json_friends.getJSONArray("data");
            for (int i=0;i<friends_list.length();++i){
                friends_list_array.add(new Pair<String, String>(friends_list.getJSONObject(i).getString("name"),friends_list.getJSONObject(i).getString("id")));
            }
            requestee_query = "requestee_fb_id=" + friends_list_array.get(0).second + "&fb_id=" + fb_id;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        fb_id = getIntent().getStringExtra("fb_id");
        access_token = getIntent().getStringExtra("access_token");
        Log.d("TAG", "onCreate: "+ friends);
        query = "fb_id="+fb_id+"&access_token="+access_token;


        new CreateLiveVideoTask().execute();
    }



    class CreateLiveVideoTask extends AsyncTask<Void, Void, String> {

        APICall apiCall = new APICall(getBaseContext());

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
                Log.d(getClass().getSimpleName(), "onPostExecute: ");
                apiCall.logDebug("String is " + s);
                JSONObject json = apiCall.convertToJsonObject(s);
                try {
                    stream_url = json.getString("stream_url");
                    secure_stream_url = json.getString("secure_stream_url");
                    videoId = json.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            Log.d("CREATE_VIDEO", "onPostExecute: "+ stream_url);
            Log.d("CREATE_VIDEO", "onPostExecute: " + secure_stream_url);
            Log.d("CREATE_VIDEO", "onPostExecute: " + videoId);

            new RequestSwitchStreamTask().execute();

        }

        @Override
        protected String doInBackground(Void... params) {

            return apiCall.request(API.GET,"create?"+query, null, null);
        }
    }


    class RequestSwitchStreamTask extends AsyncTask<Void, Void, String>{
        APICall apiCall = new APICall(getBaseContext());

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(getClass().getSimpleName(), "onPostExecute: ");
            apiCall.logDebug("String is " + s);
            Log.d("request_switch", s);
            Toast.makeText(ctx, "executing api call", Toast.LENGTH_SHORT);
            firebaseDb.child("liveVideos/" + videoId + "/currentStreamer").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("CHANGE_STREAM", "event triggered");
                    if (!dataSnapshot.getValue().equals(fb_id)){
                        // TODO: put code here to stop streaming when needed
                        Log.d("CHANGE_STREAM", "changing stream to " + dataSnapshot.getValue());
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        @Override
        protected String doInBackground(Void... params) {

            return apiCall.request(API.GET,"request_switch?"+query, null, null);
        }
    }


}

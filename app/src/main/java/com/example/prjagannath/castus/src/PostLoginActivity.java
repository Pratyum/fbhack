package com.example.prjagannath.castus.src;
import android.provider.Settings.Secure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.prjagannath.castus.R;

public class PostLoginActivity extends AppCompatActivity {

    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);

        info = (TextView)findViewById(R.id.info);

        info.setText("Hello " + getIntent().getStringExtra("friends") + ". Your token is " + getIntent().getStringExtra("token")
        + "\n\n\n\n" + Secure.getString(getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID ));
    }
}

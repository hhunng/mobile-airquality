package com.example.airqualityprojectfinish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signInActivity extends AppCompatActivity {

    public static String token = "";
    Button signInBtn, backBtn;
    APIInterface apiInterface;
    String acc, pwd = null;
    EditText acc_dt, pwd_dt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        acc_dt = (EditText) findViewById(R.id.edit_user);
        pwd_dt = (EditText) findViewById(R.id.edit_pass);
        signInBtn = findViewById(R.id.btnSignin);
        backBtn = findViewById(R.id.btnback);
        buttonEffect(backBtn);
        buttonEffect(signInBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backMainActivity = new Intent(signInActivity.this, MainActivity.class);
                startActivity(backMainActivity);
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acc = acc_dt.getText().toString();
                pwd = pwd_dt.getText().toString();
                zeroAuthTwo("openremote", "password", pwd, acc);
            }
        });
    }
    public void zeroAuthTwo (String client_id, String grant_type, String password, String username)
    {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<tokenAsset> call = apiInterface.getAsset(client_id, grant_type, password, username);
        call.enqueue(new Callback<tokenAsset>() {
            @Override
            public void onResponse(Call<tokenAsset> call, Response<tokenAsset> response) {
                Log.d("API CALL", response.code()+"");
                if (response.code() == 200) {
                    token = response.body().access_token;
                    Toast.makeText(signInActivity.this, "Successful Sign In", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(signInActivity.this, dashboard.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(signInActivity.this, "Login fail, please try again", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<tokenAsset> call, Throwable t) {
                Log.d("API CALL", t.getMessage().toString());
            }
        });
    }
    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}
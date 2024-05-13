package com.example.airqualityprojectfinish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signUpActivity extends AppCompatActivity {
    EditText username_edt, password_edt, email_edt, confirm_password_edt;
    Button signup_button, back_button;
    WebView signup_webview;
    String usr, email, pwd, rePwd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeElements();
        initializeCookies();
        buttonEffect(signup_button);
        buttonEffect(back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent directMainActivity = new Intent(signUpActivity.this, MainActivity.class);
                startActivity(directMainActivity);
            }
        });
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usr = username_edt.getText().toString();
                pwd = password_edt.getText().toString();
                email = email_edt.getText().toString();
                rePwd = confirm_password_edt.getText().toString();
                signUp();
            }
        });
    }
    private void signUp(){
        signup_webview.getSettings().setJavaScriptEnabled(true);
        signup_webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("uiot.ixxc.dev/manager/")) {
                    Toast.makeText(signUpActivity.this, "Successful Sign Up", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(signUpActivity.this, dashboard.class);
                    startActivity(intent);
                }
                else if (url.contains("uiot.ixxc.dev/auth/realms/master/login-actions/registration?session_code"))
                {
                    Toast.makeText(signUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if(url.contains("openid-connect/registrations")){
                    String usrScript =  "document.getElementById('username').value ='" + usr +"';";
                    String emailScript =  "document.getElementById('email').value ='" + email +"';";
                    String pwdScript =  "document.getElementById('password').value ='" + pwd +"';";
                    String rePwdScript =  "document.getElementById('password-confirm').value ='" + rePwd +"';";
                    String submitFormScript = "document.querySelector('form').submit();";

                    signup_webview.evaluateJavascript(usrScript, null);
                    signup_webview.evaluateJavascript(emailScript, null);
                    signup_webview.evaluateJavascript(pwdScript, null);
                    signup_webview.evaluateJavascript(rePwdScript, null);
                    signup_webview.evaluateJavascript(submitFormScript,null);

                }
            }
        });
        String url = "https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/registrations?client_id=openremote&redirect_uri=https%3A%2F%2Fuiot.ixxc.dev%2Fmanager%2F&response_mode=fragment&response_type=code&scope=openid";
        signup_webview.loadUrl(url);
    }
    private void initializeElements()
    {
        signup_button = findViewById(R.id.btn_signup);
        username_edt = findViewById(R.id.txt_1);
        password_edt = findViewById(R.id.txt_3);
        email_edt = findViewById(R.id.txt_2);
        confirm_password_edt = findViewById(R.id.txt_4);
        signup_webview = findViewById(R.id.Webview_signup);
        back_button = findViewById(R.id.btn_back);
    }
    private void initializeCookies()
    {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        WebSettings webSettings = signup_webview.getSettings();
        webSettings.setDomStorageEnabled(true);
        cookieManager.removeAllCookies(null);
        cookieManager.flush();
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
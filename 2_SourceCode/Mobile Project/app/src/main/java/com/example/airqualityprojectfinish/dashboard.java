package com.example.airqualityprojectfinish;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class dashboard extends AppCompatActivity {
    private int selectedTab = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        LinearLayout dashBoardLayout = findViewById(R.id.dashBoardButton);
        LinearLayout dbLayout = findViewById(R.id.dbButton);
        LinearLayout userLayout = findViewById(R.id.userButton);
        LinearLayout chartLayout = findViewById(R.id.chartButton);

        ImageView dashBoardImage = findViewById(R.id.dashBoardIcon);
        ImageView dbImage = findViewById(R.id.dbIcon);
        ImageView userImage = findViewById(R.id.userIcon);
        ImageView chartImage = findViewById(R.id.chartIcon);

        TextView dashBoardTextView = findViewById(R.id.dashBoardText);
        TextView dbTextView = findViewById(R.id.dbText);
        TextView userTextView = findViewById(R.id.userText);
        TextView chartTextView = findViewById(R.id.chartText);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, homeFragment.class, null)
                .commit();
        dashBoardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTab != 1) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, homeFragment.class, null)
                            .commit();
                    dbTextView.setVisibility(View.GONE);
                    userTextView.setVisibility(View.GONE);
                    chartTextView.setVisibility(View.GONE);

                    dbImage.setImageResource(R.drawable.map);
                    userImage.setImageResource(R.drawable.usersetting_icon);
                    chartImage.setImageResource(R.drawable.chart_icon);

                    dbLayout.setBackgroundColor(Color.TRANSPARENT);
                    userLayout.setBackgroundColor(Color.TRANSPARENT);
                    chartLayout.setBackgroundColor(Color.TRANSPARENT);

                    dashBoardTextView.setVisibility(View.VISIBLE);
                    dashBoardImage.setImageResource(R.drawable.selected_dashboard);
                    dashBoardLayout.setBackgroundResource(R.drawable.round_back);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    dashBoardLayout.startAnimation(scaleAnimation);

                    selectedTab = 1;

                }
            }
        });

        dbLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTab != 2) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, dbFragment.class, null)
                            .commit();
                    dashBoardTextView.setVisibility(View.GONE);
                    userTextView.setVisibility(View.GONE);
                    chartTextView.setVisibility(View.GONE);

                    dashBoardImage.setImageResource(R.drawable.dashboard_icon);
                    userImage.setImageResource(R.drawable.usersetting_icon);
                    chartImage.setImageResource(R.drawable.chart_icon);

                    dashBoardLayout.setBackgroundColor(Color.TRANSPARENT);
                    userLayout.setBackgroundColor(Color.TRANSPARENT);
                    chartLayout.setBackgroundColor(Color.TRANSPARENT);

                    dbTextView.setVisibility(View.VISIBLE);
                    dbImage.setImageResource(R.drawable.selected_map);
                    dbLayout.setBackgroundResource(R.drawable.db_round_back);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    dbLayout.startAnimation(scaleAnimation);

                    selectedTab = 2;

                }
            }
        });
        userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTab != 3) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, userFragment.class, null)
                            .commit();
                    dbTextView.setVisibility(View.GONE);
                    dashBoardTextView.setVisibility(View.GONE);
                    chartTextView.setVisibility(View.GONE);

                    dbImage.setImageResource(R.drawable.map);
                    dashBoardImage.setImageResource(R.drawable.dashboard_icon);
                    chartImage.setImageResource(R.drawable.chart_icon);

                    dbLayout.setBackgroundColor(Color.TRANSPARENT);
                    dashBoardImage.setBackgroundColor(Color.TRANSPARENT);
                    chartLayout.setBackgroundColor(Color.TRANSPARENT);

                    userTextView.setVisibility(View.VISIBLE);
                    userImage.setImageResource(R.drawable.selected_user_setting);
                    userLayout.setBackgroundResource(R.drawable.user_round_back);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    userLayout.startAnimation(scaleAnimation);

                    selectedTab = 3;

                }
            }
        });
        chartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTab != 4) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, chartFragment.class, null)
                            .commit();
                    dbTextView.setVisibility(View.GONE);
                    userTextView.setVisibility(View.GONE);
                    dashBoardTextView.setVisibility(View.GONE);

                    dbImage.setImageResource(R.drawable.map);
                    userImage.setImageResource(R.drawable.usersetting_icon);
                    dashBoardImage.setImageResource(R.drawable.dashboard_icon);

                    dbLayout.setBackgroundColor(Color.TRANSPARENT);
                    userLayout.setBackgroundColor(Color.TRANSPARENT);
                    dashBoardLayout.setBackgroundColor(Color.TRANSPARENT);

                    chartTextView.setVisibility(View.VISIBLE);
                    chartImage.setImageResource(R.drawable.selected_chart);
                    chartLayout.setBackgroundResource(R.drawable.chart_round_back);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    chartLayout.startAnimation(scaleAnimation);

                    selectedTab = 4;

                }
            }
        });

    }
}
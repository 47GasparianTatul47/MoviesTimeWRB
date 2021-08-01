package myActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.example.moviestime.R;

public class WelcomeActivity extends AppCompatActivity {

    Animation animation;
    ImageView appLogo;
    ImageView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        appLogo = findViewById(R.id.appLogo);
        appName = findViewById(R.id.appName);

        appLogo.setVisibility(View.GONE);
        appName.setVisibility(View.GONE);

        animation = AnimationUtils.loadAnimation(this, R.anim.welcomeanim);


        appLogo.setAnimation(animation);
        appName.setAnimation(animation);


        Handler hAnim = new Handler();

        hAnim.postDelayed(() -> {
            appLogo.setVisibility(View.VISIBLE);
            appName.setVisibility(View.VISIBLE);
            animation.start();
        }, 1000);


        Handler handler = new Handler();
        handler.postDelayed(() -> {

            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        }, 1200);

    }
}

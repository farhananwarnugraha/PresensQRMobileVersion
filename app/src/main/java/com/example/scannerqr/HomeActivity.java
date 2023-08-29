package com.example.scannerqr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageizin;
    private  ImageView imaginfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siswadashboard);

        findViewById(R.id.imageizin).setOnClickListener(this);
        findViewById(R.id.imaginfo).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageizin:
                Intent intent = new Intent(this, izinActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.imaginfo:
                Intent intent1 = new Intent(this, infoActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }
}
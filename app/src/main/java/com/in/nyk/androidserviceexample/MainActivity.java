package com.in.nyk.androidserviceexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonStart, buttonStop,buttonNext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonNext = (Button) findViewById(R.id.buttonNext);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
    }
    public void onClick(View src) {
        Intent service = new Intent(MainActivity.this, ForeGroundService.class);
        switch (src.getId()) {
            case R.id.buttonStart:
               // startService(new Intent(this, MyService.class));
                service.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                ForeGroundService.IS_SERVICE_RUNNING = true;
                startService(service);
                break;
            case R.id.buttonStop:
                //stopService(new Intent(this, MyService.class));
                service.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                ForeGroundService.IS_SERVICE_RUNNING = false;
                startService(service);
                break;
            case R.id.buttonNext:
                Intent intent=new Intent(this,BindwithMessangerActivity.class);
                startActivity(intent);
                break;
        }
    }
}

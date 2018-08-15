package com.bus.lifecircleeventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LifeCircleEventBus.with("test").observe(this.getClass(), new LifeCircleEventBus.Observer<String>() {
            @Override
            public void onchange(String value) {
                Toast.makeText(MainActivity.this, value + MainActivity.class, Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LifeCircleEventBus.with("test").setValue("测试");
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}

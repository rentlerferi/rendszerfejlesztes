package com.example.api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class OperatorMenu extends AppCompatActivity {

    Button Bt1,Bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_menu);

        Bt1 = findViewById(R.id.Bt1);
        Bt2 = findViewById(R.id.Bt2);

        Bt1.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(OperatorMenu.this, OperatorAssign.class);
            startActivity(i);
        });

        Bt2.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(OperatorMenu.this, EmergencyTasks.class);
            startActivity(i);
        });
    }


}
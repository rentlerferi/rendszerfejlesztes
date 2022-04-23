package com.example.api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ToolCorrespondentMenu extends AppCompatActivity {

    Button group, certain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_correspondent_menu);

        group = findViewById(R.id.toolGroup);
        certain = findViewById(R.id.certainTool);

        certain.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(ToolCorrespondentMenu.this, ToolCorrespondentCertain.class);
            startActivity(i);
        });

        group.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(ToolCorrespondentMenu.this, ToolCorrespondentCategory.class);
            startActivity(i);
        });
    }
}
package com.example.api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ToolCorresponentMenu extends AppCompatActivity {

    Button group, certain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_correspondent_menu);

        group = findViewById(R.id.toolGroup);
        certain = findViewById(R.id.certainTool);

        certain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(ToolCorresponentMenu.this, ToolCorrespondentCertain.class);
                startActivity(i);
            }
        });

        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(ToolCorresponentMenu.this, ToolCorrespondentCategory.class);
                startActivity(i);
            }
        });
    }
}
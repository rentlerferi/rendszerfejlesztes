package com.example.api;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminMenu extends AppCompatActivity {

    Button newUser, addProfession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        newUser = findViewById(R.id.addUser);
        addProfession = findViewById(R.id.addProfession);

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(AdminMenu.this, Admin.class);
                startActivity(i);
            }
        });

        addProfession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(AdminMenu.this, AddProfession.class);
                startActivity(i);
            }
        });
    }
}
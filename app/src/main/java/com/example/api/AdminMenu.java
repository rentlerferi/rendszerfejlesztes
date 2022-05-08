package com.example.api;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminMenu extends AppCompatActivity {

    Button newUser, editUser,addProfession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        newUser = findViewById(R.id.addUser);
        editUser = findViewById(R.id.editUser);
        addProfession = findViewById(R.id.addProfession);

        newUser.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(AdminMenu.this, AddUser.class);
            startActivity(i);
        });

        editUser.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(AdminMenu.this, EditUser.class);
            startActivity(i);
        });
        addProfession.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(AdminMenu.this, AddProfession.class);
            startActivity(i);
        });

    }
}
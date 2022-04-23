package com.example.api;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Admin extends AppCompatActivity {

    EditText Name, ID, Knw1, Knw2, Knw3, Knw4;
    Spinner Role;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Name = findViewById(R.id.Name);
        ID = findViewById(R.id.ID);
        Knw1 = findViewById(R.id.Knw1);
        Knw2 = findViewById(R.id.Knw2);
        Knw3 = findViewById(R.id.Knw3);
        Knw4 = findViewById(R.id.Knw4);
        Role = findViewById(R.id.Role);
        ref = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url)).getReference("Users");
    }

    public void createUser(View view) {
        String name = Name.getText().toString();
        String id = ID.getText().toString().toUpperCase().trim();
        String role = Role.getSelectedItem().toString();
        ArrayList<String> knows = new ArrayList<>();

        if (!Knw1.getText().toString().equals("")) {
            knows.add(Knw1.getText().toString());

        }
        if (!Knw2.getText().toString().equals("")) {
            knows.add(Knw2.getText().toString());

        }
        if (!Knw3.getText().toString().equals("")) {
            knows.add(Knw3.getText().toString());

        }
        if (!Knw4.getText().toString().equals("")) {
            knows.add(Knw4.getText().toString());

        }

        User user = new User(knows, name, role);

        if (!id.equals("") && !role.equals("")) {
            DatabaseReference userRef = ref.child(id);
            userRef.setValue(user);
        }
    }

}
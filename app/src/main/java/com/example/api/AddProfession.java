package com.example.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class AddProfession extends AppCompatActivity {

    EditText knowledge;
    Button add, delete;
    Spinner usersSpinner, professionSpinner;

    User user;
    String profession;

    ArrayList<String> userIDs = new ArrayList<>();
    ArrayList<String> userProfessions = new ArrayList<>();

    ArrayAdapter<String> userAdapter, profAdapter;

    DatabaseReference ref, userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profession);

        usersSpinner = findViewById(R.id.usersSpinner);
        professionSpinner = findViewById(R.id.professionSpinner);
        knowledge = findViewById(R.id.knowledge);
        add = findViewById(R.id.addProfession);
        delete = findViewById(R.id.delProfession);

        ref = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url)).getReference("Users");

        userAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, userIDs);
        profAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, userProfessions);
        usersSpinner.setAdapter(userAdapter);
        professionSpinner.setAdapter(profAdapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userIDs.clear();
                for (DataSnapshot item: snapshot.getChildren()) {
                    userIDs.add(item.getKey());
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        usersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id = usersSpinner.getSelectedItem().toString();
                userRef = ref.child(id);
                updateProfessions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        add.setOnClickListener(view -> {
            profession = knowledge.getText().toString();
            if (userProfessions.contains(profession)) return;
            user.addProfession(profession);
            userRef.setValue(user);
            updateProfessions();
        });

        delete.setOnClickListener(view -> {
            profession = professionSpinner.getSelectedItem().toString();
            user.removeProfession(profession);
            userRef.setValue(user);
            updateProfessions();
        });
    }

    private void updateProfessions(){
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                userProfessions.clear();
                userProfessions.addAll(user.getProfession());
                profAdapter.notifyDataSetChanged();

                if (userProfessions.isEmpty()){
                    professionSpinner.setEnabled(false);
                    delete.setEnabled(false);
                }
                else {
                    professionSpinner.setEnabled(true);
                    delete.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
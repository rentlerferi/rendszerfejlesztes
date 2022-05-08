package com.example.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditUser extends AppCompatActivity {


    Button add, delete;
    Spinner usersSpinner, professionSpinner, knowledgeSpinner;

    User user;
    String profession;

    ArrayList<String> userIDs = new ArrayList<>();
    ArrayList<String> userProfessions = new ArrayList<>();
    ArrayList<String> availableProfessions = new ArrayList<>();

    ArrayAdapter<String> userAdapter, profAdapter, avaAdapter;

    DatabaseReference ref, userRef,usersRef,profRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        usersSpinner = findViewById(R.id.usersSpinner);
        professionSpinner = findViewById(R.id.professionSpinner);
        knowledgeSpinner = findViewById(R.id.knowledgeSpinner);
        add = findViewById(R.id.editUser);
        delete = findViewById(R.id.delProfession);

        ref = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url)).getReference();
        usersRef = ref.child("Users");
        profRef = ref.child("Professions");

        userAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, userIDs);
        profAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, userProfessions);
        avaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, availableProfessions);
        usersSpinner.setAdapter(userAdapter);
        professionSpinner.setAdapter(profAdapter);
        knowledgeSpinner.setAdapter(avaAdapter);

        usersRef.addValueEventListener(new ValueEventListener() {
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
                userRef = usersRef.child(id);
                updateRefs();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        add.setOnClickListener(view -> {
            profession = knowledgeSpinner.getSelectedItem().toString();
            if (userProfessions.contains(profession)) return;
            user.addProfession(profession);
            userRef.setValue(user);
            updateRefs();
        });

        delete.setOnClickListener(view -> {
            profession = professionSpinner.getSelectedItem().toString();
            user.removeProfession(profession);
            userRef.setValue(user);
            updateRefs();
        });
    }

    private void updateRefs(){
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                userProfessions.clear();
                userProfessions.addAll(user.getProfessions());
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

        profRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                availableProfessions.clear();
                for(DataSnapshot s : snapshot.getChildren()){
                    if(!userProfessions.contains(s.getKey())){
                        availableProfessions.add(s.getKey());
                    }
                }
                avaAdapter.notifyDataSetChanged();

                if (availableProfessions.isEmpty()){
                    add.setEnabled(false);
                    knowledgeSpinner.setEnabled(false);
                }
                else {
                    knowledgeSpinner.setEnabled(true);
                    add.setEnabled(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
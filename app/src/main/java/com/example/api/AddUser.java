package com.example.api;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddUser extends AppCompatActivity {

    EditText Name, ID;
    Spinner Role, addProfessionSpinner, addedProfessionsSpinner;
    SeekBar workingHours;
    TextView workingHoursTW;
    Button addProfessionButton, deleteProfessionButton;

    DatabaseReference ref, usersRef, profRef;

    ArrayList<String> professions = new ArrayList<>();
    ArrayList<String> addedProfessions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Name = findViewById(R.id.Name);
        ID = findViewById(R.id.ID);
        Role = findViewById(R.id.Role);
        addProfessionSpinner = findViewById(R.id.addProfessionSpinner);
        addedProfessionsSpinner = findViewById(R.id.addedProfessionsSpinner);
        workingHours = findViewById(R.id.workingHours);
        workingHoursTW = findViewById(R.id.workingHoursTW);
        addProfessionButton = findViewById(R.id.addProfessionButton);
        deleteProfessionButton = findViewById(R.id.deleteProfessionButton);

        ref = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url)).getReference();
        usersRef = ref.child("Users");
        profRef = ref.child("Professions");

        ArrayAdapter<String> prof_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, professions);
        ArrayAdapter<String> added_prof_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, addedProfessions);
        addProfessionSpinner.setAdapter(prof_adapter);
        addedProfessionsSpinner.setAdapter(added_prof_adapter);

        profRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                professions.clear();
                for (DataSnapshot s: snapshot.getChildren()) {
                    professions.add(s.getKey());
                }
                prof_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addProfessionButton.setOnClickListener(t -> {
            if(!addedProfessions.contains(addProfessionSpinner.getSelectedItem().toString())){
                addedProfessions.add(addProfessionSpinner.getSelectedItem().toString());
                added_prof_adapter.notifyDataSetChanged();
            }
        });

        deleteProfessionButton.setOnClickListener(t -> {
            if(addedProfessions.contains(addedProfessionsSpinner.getSelectedItem().toString())){
                addedProfessions.remove(addedProfessionsSpinner.getSelectedItem().toString());
                added_prof_adapter.notifyDataSetChanged();
            }
        });

        workingHours.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                workingHoursTW.setText("Working Hours (" + String.valueOf(progress  + 1) + ")");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void createUser(View view) {
        String name = Name.getText().toString();
        String id = ID.getText().toString().toUpperCase().trim();
        String role = Role.getSelectedItem().toString();

        User user = new User(addedProfessions, name, role, workingHours.getProgress() + 1);

        if (!id.equals("") && !role.equals("")) {
            DatabaseReference userRef = usersRef.child(id);
            userRef.setValue(user);
        }
    }

}
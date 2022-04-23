package com.example.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Operator extends AppCompatActivity {

    Spinner repairers_list,tasks_list;
    Button asingBt , unsignBt;
    ArrayList<String> repairer_array_list,task_array_list;

    DatabaseReference ref,rep_ref,task_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        repairers_list=findViewById(R.id.repairer_list);
        tasks_list=findViewById(R.id.tasks_list);
        asingBt=findViewById(R.id.button8);
        unsignBt=findViewById(R.id.button9);

        ref = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url)).getReference();

        rep_ref=ref.child("Users");
        task_ref=ref.child("Tasks");


        repairer_array_list = new ArrayList<>();
        task_array_list = new ArrayList<>();
        ArrayAdapter<String> rep_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, repairer_array_list);
        ArrayAdapter<String>tas_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, task_array_list);

        rep_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item: snapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if(user.getRole().equals("Repairer"))
                        repairer_array_list.add(item.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        task_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item: snapshot.getChildren()) {
                    

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tasks_list.setAdapter(tas_adapter);
        repairers_list.setAdapter(rep_adapter);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);
    }
}
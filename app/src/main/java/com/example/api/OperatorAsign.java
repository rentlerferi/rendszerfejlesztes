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

public class OperatorAsign extends AppCompatActivity {

    Spinner repairers_list,tasks_list;
    Button asingBt , unsignBt;
    ArrayList<String> repairer_array_list,task_array_list;

    FirebaseDatabase fb;
    DatabaseReference rep_ref,task_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_asign);

        repairers_list=findViewById(R.id.repairer_list);
        tasks_list=findViewById(R.id.tasks_list);
        asingBt=findViewById(R.id.button8);
        unsignBt=findViewById(R.id.button9);

        fb= FirebaseDatabase.getInstance(getResources().getString(R.string.database_url));

        rep_ref=fb.getReference("User");
        task_ref=fb.getReference("Tasks");


        repairer_array_list = new ArrayList<>();
        task_array_list = new ArrayList<>();

        ArrayAdapter<String> rep_adapter = new ArrayAdapter<String>(OperatorAsign.this, android.R.layout.simple_spinner_dropdown_item, repairer_array_list);
        ArrayAdapter<String> tas_adapter = new ArrayAdapter<String>(OperatorAsign.this, android.R.layout.simple_spinner_dropdown_item, task_array_list);

        tasks_list.setAdapter(tas_adapter);
        repairers_list.setAdapter(rep_adapter);

        rep_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                repairer_array_list.clear();
                for (DataSnapshot item: snapshot.getChildren()) {
                    User user = item.getValue(User.class);
                    if(user.getRole().equals("Repairer"))
                        repairer_array_list.add(item.getKey());
                }
                rep_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // task list feltöltés

        task_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                task_array_list.clear();
                for (DataSnapshot item: snapshot.getChildren()) {
                    

                }
                tas_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }
}
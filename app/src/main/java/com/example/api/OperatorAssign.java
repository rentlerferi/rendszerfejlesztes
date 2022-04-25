package com.example.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class OperatorAssign extends AppCompatActivity {

    Spinner repairers_list,tasks_list;
    Button assignBt, unassignBt;
    ArrayList<String> repairer_array_list,task_array_list;

    TextView itemE, dateE, locationE, instructionE, statusE, emergencyE;
    String item, date, location, instruction, status, emergency;

    FirebaseDatabase fb;
    DatabaseReference rep_ref,task_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_assign);

        repairers_list=findViewById(R.id.repairer_list);
        tasks_list=findViewById(R.id.tasks_list);
        assignBt =findViewById(R.id.assignBtn);
        unassignBt =findViewById(R.id.unassignBtn);
        itemE = findViewById(R.id.itemName);
        dateE = findViewById(R.id.itemDate);
        locationE = findViewById(R.id.itemLocation);
        instructionE = findViewById(R.id.itemInstruction);
        statusE = findViewById(R.id.itemStatus);
        emergencyE = findViewById(R.id.itemEmOrNot);

        fb= FirebaseDatabase.getInstance(getResources().getString(R.string.database_url));

        rep_ref=fb.getReference("Users");
        task_ref=fb.getReference("Tasks");

        repairer_array_list = new ArrayList<>();
        task_array_list = new ArrayList<>();

        ArrayAdapter<String> rep_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, repairer_array_list);
        ArrayAdapter<String> tas_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, task_array_list);

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
                    task_array_list.add(item.getKey());
                }
                tas_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "loadPost:onCancelled", error.toException());
            }
        });

        tasks_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                task_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Task task = snapshot.getValue(Task.class);
                            if (Objects.equals(snapshot.getKey(), tasks_list.getSelectedItem().toString())) {
                                itemE.setText("Task name: " + task.getItemName());
                                dateE.setText("Task date: " + task.getDate());
                                locationE.setText("Task location: " + task.getLocation());
                                instructionE.setText("Task instruction: " + task.getInstruction());
                                statusE.setText("Task status: " + task.getStatus());
                                if(String.valueOf(task.isEmergency()).equals(false)){
                                    emergencyE.setText("Not an emergency");
                                } else {
                                    emergencyE.setText("Emergency");
                                }
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("TAG", "loadPost:onCancelled", error.toException());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
package com.example.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OperatorAssign extends AppCompatActivity {

    Spinner repairerSpinner, taskSpinner, professionFilterSpinner;
    Button assignBt;
    HashMap<String, User> repairer_map = new HashMap<>();
    HashMap<String, Task> tasks = new HashMap<>();
    ArrayList<String> filtered_repairer_array_list,task_array_list, professions_array_list;

    TextView itemE, dateE, locationE, instructionE, statusE, emergencyE, normaE;

    FirebaseDatabase fb;
    DatabaseReference rep_ref,task_ref, prof_ref;

    Task selectedTask;

    ArrayAdapter<String> rep_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_assign);

        repairerSpinner =findViewById(R.id.repairerSpinner);
        taskSpinner=findViewById(R.id.taskSpinner);
        professionFilterSpinner = findViewById(R.id.professionFilterSpinner);
        assignBt =findViewById(R.id.assignBtn);
        itemE = findViewById(R.id.itemName);
        dateE = findViewById(R.id.itemDate);
        locationE = findViewById(R.id.itemLocation);
        instructionE = findViewById(R.id.itemInstruction);
        statusE = findViewById(R.id.itemStatus);
        emergencyE = findViewById(R.id.itemEmOrNot);
        normaE = findViewById(R.id.itemNorma);

        fb= FirebaseDatabase.getInstance(getResources().getString(R.string.database_url));

        rep_ref = fb.getReference("Users");
        task_ref = fb.getReference("Tasks");
        prof_ref = fb.getReference("Professions");


        task_array_list = new ArrayList<>();
        professions_array_list = new ArrayList<>();
        filtered_repairer_array_list = new ArrayList<>();

        rep_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtered_repairer_array_list);
        ArrayAdapter<String> tas_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, task_array_list);
        ArrayAdapter<String> prof_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, professions_array_list);

        taskSpinner.setAdapter(tas_adapter);
        repairerSpinner.setAdapter(rep_adapter);
        professionFilterSpinner.setAdapter(prof_adapter);

        prof_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                professions_array_list.clear();
                for (DataSnapshot s : snapshot.getChildren()){
                    professions_array_list.add(s.getKey());
                }
                prof_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rep_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                repairer_map.clear();
                for (DataSnapshot item: snapshot.getChildren()) {
                    User user = item.getValue(User.class);
                    if(user.role.equals("Repairer"))
                        repairer_map.put(item.getKey(), user);
                }
                rep_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        task_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                task_array_list.clear();
                tasks.clear();
                for (DataSnapshot item: snapshot.getChildren()) {
                    Task task = item.getValue(Task.class);
                    tasks.put(item.getKey(), task);
                    if(task.status.equals("Unassigned"))
                        task_array_list.add(item.getKey());
                }

                if(!task_array_list.isEmpty()) {
                    itemE.setText("Task name: " + tasks.get(task_array_list.get(0)).itemName);
                    dateE.setText("Task date: " + tasks.get(task_array_list.get(0)).date);
                    locationE.setText("Task location: " + tasks.get(task_array_list.get(0)).location);
                    instructionE.setText("Task instruction: " + tasks.get(task_array_list.get(0)).instruction);
                    statusE.setText("Task status: " + tasks.get(task_array_list.get(0)).status);
                    normaE.setText("Task norma: " + tasks.get(task_array_list.get(0)).norma);
                    String emergencyStr = (tasks.get(task_array_list.get(0)).isEmergency) ? "Emergency" : "Not an emergency";
                    emergencyE.setText(emergencyStr);
                    selectedTask = tasks.get(task_array_list.get(0));
                }
                else {
                    itemE.setText("");
                    dateE.setText("");
                    locationE.setText("");
                    instructionE.setText("");
                    statusE.setText("");
                    normaE.setText("");
                    emergencyE.setText("");
                }

                tas_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        taskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTask = tasks.get(taskSpinner.getSelectedItem().toString());
                itemE.setText("Task name: " + selectedTask.itemName);
                dateE.setText("Task date: " + selectedTask.date);
                locationE.setText("Task location: " + selectedTask.location);
                instructionE.setText("Task instruction: " + selectedTask.instruction);
                statusE.setText("Task status: " + selectedTask.status);
                normaE.setText("Task norma: " + selectedTask.norma);
                String emergencyStr = (selectedTask.isEmergency) ? "Emergency" : "Not an emergency";
                emergencyE.setText(emergencyStr);
                FilterRepairers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        assignBt.setOnClickListener(task -> {
            if(task_array_list.isEmpty()) return;
            String selectedUser = repairerSpinner.getSelectedItem().toString();
            String selectedTaskstr = taskSpinner.getSelectedItem().toString();
            tasks.get(taskSpinner.getSelectedItem().toString()).repairerID = selectedUser;
            tasks.get(taskSpinner.getSelectedItem().toString()).status = "Assigned";
            task_ref.child(selectedTaskstr).setValue(tasks.get(taskSpinner.getSelectedItem().toString()));
            FilterRepairers();
        });

        professionFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FilterRepairers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FilterRepairers();
    }

    private void FilterRepairers(){
        if(selectedTask == null) return;

        filtered_repairer_array_list.clear();
        for (Map.Entry<String, User> p : repairer_map.entrySet()) {
            if (p.getValue().getProfessions().contains(professionFilterSpinner.getSelectedItem().toString())){
                int hours = 0;
                for (Map.Entry<String, Task> e : tasks.entrySet()){
                    if(e.getValue().repairerID.equals(p.getKey())){
                        hours += e.getValue().norma;
                    }
                }
                if(hours + selectedTask.norma <= p.getValue().shiftTime)
                    filtered_repairer_array_list.add(p.getKey());
            }
        }

        if (filtered_repairer_array_list.isEmpty()) assignBt.setEnabled(false);
        else assignBt.setEnabled(true);

        rep_adapter.notifyDataSetChanged();
    }
}
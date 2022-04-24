package com.example.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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

public class Repairer extends AppCompatActivity {

    String uId;
    Spinner incomingTasks;
    TextView taskName, taskLocation, taskInstruction, taskDate, isEmergency;
    ListView toDoTasks;
    Button acceptTask, denyTask;

    ArrayList<String> incomingTaskIDs = new ArrayList<>();
    ArrayList<String> acceptedTaskIDs = new ArrayList<>();
    HashMap<String, Task> tasks = new HashMap<>();

    FirebaseDatabase fb;
    DatabaseReference tasksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairer);

        uId = getIntent().getStringExtra("id");

        incomingTasks = findViewById(R.id.incomingTasks);
        taskName = findViewById(R.id.taskName);
        taskLocation = findViewById(R.id.taskLocation);
        taskInstruction = findViewById(R.id.taskInstruction);
        taskDate = findViewById(R.id.taskDate);
        isEmergency = findViewById(R.id.isEmergency);
        toDoTasks = findViewById(R.id.toDoTasks);
        acceptTask = findViewById(R.id.acceptTask);
        denyTask = findViewById(R.id.denyTask);

        fb = FirebaseDatabase.getInstance(getResources().getString(R.string.database_url));
        tasksRef = fb.getReference("Tasks");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, incomingTaskIDs);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, acceptedTaskIDs);

        incomingTasks.setAdapter(spinnerAdapter);
        toDoTasks.setAdapter(listViewAdapter);

        tasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearLists();

                for (DataSnapshot item : snapshot.getChildren()) {
                    tasks.put(item.getKey(), item.getValue(Task.class));
                }

                sortTasks();
                spinnerAdapter.notifyDataSetChanged();
                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        acceptTask.setOnClickListener(task -> {
            if(incomingTasks.getSelectedItemPosition() < 0) return;
            String taskID = incomingTasks.getSelectedItem().toString();
            DatabaseReference statusRef = tasksRef.child(taskID).child("status");
            statusRef.setValue("InProgress");
        });

        denyTask.setOnClickListener(task -> {
            if(incomingTasks.getSelectedItemPosition() < 0) return;
            String taskID = incomingTasks.getSelectedItem().toString();
            DatabaseReference repairerRef = tasksRef.child(taskID).child("repairerID");
            DatabaseReference statusRef = tasksRef.child(taskID).child("status");
            repairerRef.setValue("");
            statusRef.setValue("Unassigned");
        });

        incomingTasks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Task t = tasks.get(incomingTasks.getItemAtPosition(i));
                taskName.setText("Task Name: " + t.itemName);
                taskLocation.setText("Task Location: " + t.location);
                taskInstruction.setText("Task Instruction: " + t.instruction);
                taskDate.setText("Task Date: " + t.date);
                if (t.isEmergency) isEmergency.setText("Emergency");
                else isEmergency.setText("Not Emergency");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                taskName.setText("");
                taskLocation.setText("");
                taskInstruction.setText("");
                taskDate.setText("");
                isEmergency.setText("");
            }
        });

        toDoTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pair<String, Task> t = new Pair<>((String)toDoTasks.getItemAtPosition(i), tasks.get((String)toDoTasks.getItemAtPosition(i)));

                AlertDialog.Builder builder = new AlertDialog.Builder(Repairer.this);
                builder.setTitle(t.first);
                String sIsEmergency = (t.second.isEmergency) ? "Emergency" : "Not Emergency";
                builder.setMessage(String.format("Task Name: %s\nTask Location: %s\nTask Instruction: %s\nTask Date: %s\n%s",
                        t.second.itemName, t.second.location, t.second.instruction, t.second.date, sIsEmergency));
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Task Done",
                        (dialog, id) -> {
                            DatabaseReference statusRef = tasksRef.child(t.first).child("status");
                            statusRef.setValue("Done");
                            dialog.cancel();
                        });

                builder.setNegativeButton(
                        "Cancel",
                        (dialog, id) -> dialog.cancel());

                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });
    }

    @SuppressLint("NewApi")
    private void sortTasks(){
        tasks.forEach((key, value) -> {
            if (value.repairerID.equals(uId)){
                if(value.status.equals("Assigned")){
                    incomingTaskIDs.add(key);
                } else if (value.status.equals("InProgress")){
                    acceptedTaskIDs.add(key);
                }
            }
        });
    }

    private void clearLists(){
        incomingTaskIDs.clear();
        acceptedTaskIDs.clear();
        tasks.clear();
    }
}
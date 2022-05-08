package com.example.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class OperatorAssign extends AppCompatActivity {

    Spinner repairerSpinner, taskSpinner;
    Button assignBt;
    ArrayList<String> repairer_array_list,task_array_list;

    TextView itemE, dateE, locationE, instructionE, statusE, emergencyE;

    FirebaseDatabase fb;
    DatabaseReference rep_ref,task_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_assign);

        repairerSpinner =findViewById(R.id.repairerSpinner);
        taskSpinner=findViewById(R.id.taskSpinner);
        assignBt =findViewById(R.id.assignBtn);
        itemE = findViewById(R.id.itemName);
        dateE = findViewById(R.id.itemDate);
        locationE = findViewById(R.id.itemLocation);
        instructionE = findViewById(R.id.itemInstruction);
        statusE = findViewById(R.id.itemStatus);
        emergencyE = findViewById(R.id.itemEmOrNot);


        fb= FirebaseDatabase.getInstance(getResources().getString(R.string.database_url));

        rep_ref = fb.getReference("Users");
        task_ref = fb.getReference("Tasks");

        repairer_array_list = new ArrayList<>();
        task_array_list = new ArrayList<>();

        ArrayAdapter<String> rep_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, repairer_array_list);
        ArrayAdapter<String> tas_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, task_array_list);

        taskSpinner.setAdapter(tas_adapter);
        repairerSpinner.setAdapter(rep_adapter);

        rep_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                repairer_array_list.clear();
                for (DataSnapshot item: snapshot.getChildren()) {
                    User user = item.getValue(User.class);
                    if(user.role.equals("Repairer"))
                        repairer_array_list.add(item.getKey());
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
                for (DataSnapshot item: snapshot.getChildren()) {
                    Task task = item.getValue(Task.class);
                    if(task.status.equals("Unassigned"))
                        task_array_list.add(item.getKey());
                }
                tas_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        taskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                taskSpinner.setEnabled(true);
                assignBt.setEnabled(true);

                task_ref.child(taskSpinner.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Task task = snapshot.getValue(Task.class);
                        itemE.setText("Task name: " + task.itemName);
                        dateE.setText("Task date: " + task.date);
                        locationE.setText("Task location: " + task.location);
                        instructionE.setText("Task instruction: " + task.instruction);
                        statusE.setText("Task status: " + task.status);
                        String emergencyStr = (task.isEmergency) ? "Emergency" :"Not an emergency";
                        emergencyE.setText(emergencyStr);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                taskSpinner.setEnabled(false);
                assignBt.setEnabled(false);
            }
        });

        assignBt.setOnClickListener(task -> {
            String selectedUser = repairerSpinner.getSelectedItem().toString();
            String selectedTask = taskSpinner.getSelectedItem().toString();

            task_ref.child(selectedTask).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Task t = snapshot.getValue(Task.class);
                    t.repairerID = selectedUser;
                    t.status = "Assigned";
                    task_ref.child(selectedTask).setValue(t);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });
    }
}
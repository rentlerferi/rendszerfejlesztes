package com.example.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.HashMap;

public class EmergencyTasks extends AppCompatActivity {

    Spinner list;
    EditText instructions;
    CalendarView date;
    TextView location;
    ArrayList<String> all_tools;
    Button BuTtOn1;

    FirebaseDatabase fb;
    DatabaseReference tool_ref,task_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_tasks);

        BuTtOn1=findViewById(R.id.commitBttn);
        list=findViewById(R.id.itemList);
        instructions=findViewById(R.id.instructions);
        date=findViewById(R.id.date);
        location=findViewById(R.id.Location);

        fb= FirebaseDatabase.getInstance(getResources().getString(R.string.database_url));
        tool_ref=fb.getReference("Tool Categories");
        task_ref=fb.getReference("Task");

        all_tools = new ArrayList<>();
        ArrayAdapter<String> tool_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, all_tools);
        list.setAdapter(tool_adapter);
        HashMap<String,String> location_hess = new HashMap<>();

        tool_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                all_tools.clear();
                location_hess.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    DatabaseReference temp=tool_ref.child(item.getKey()).child("Tools");
                    temp.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            for (DataSnapshot tool:snapshot1.getChildren()) {
                                all_tools.add(tool.getKey());
                                location_hess.put(tool.getKey(),tool.getValue(Tool.class).getLocation());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                tool_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String idy = list.getItemAtPosition(i).toString();
                location.setText("Location ->  "+location_hess.get(idy));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                location.setText("");
            }
        });



        BuTtOn1.setOnClickListener(view -> {

        });



    }

}
package com.example.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmergencyTasks extends AppCompatActivity {

    Spinner list;
    EditText instructions;
    CalendarView date;
    TextView location;
    ArrayList<String> all_tools;

    FirebaseDatabase fb;
    DatabaseReference tool_ref,task_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_tasks);

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

        tool_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                all_tools.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    for (DataSnapshot sec_item:item.getChildren() ) {
                        all_tools.add(sec_item.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}